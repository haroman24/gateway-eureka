package cl.duoc.rrhh.nominas.service;

import cl.duoc.rrhh.nominas.dto.request.CrearNominaRequest;
import cl.duoc.rrhh.nominas.dto.response.NominaResponse;
import cl.duoc.rrhh.nominas.entity.EstadoPago;
import cl.duoc.rrhh.nominas.entity.Nomina;
import cl.duoc.rrhh.nominas.exception.BusinessRuleException;
import cl.duoc.rrhh.nominas.exception.DuplicateResourceException;
import cl.duoc.rrhh.nominas.repository.NominaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NominaServiceTest {

    @Mock
    private NominaRepository nominaRepository;

    @InjectMocks
    private NominaService nominaService;

    @Test
    void crearDebeCalcularSueldoLiquido() {
        CrearNominaRequest request = new CrearNominaRequest(
                3L,
                "2026-06",
                new BigDecimal("1200000"),
                new BigDecimal("150000"),
                new BigDecimal("95000")
        );

        when(nominaRepository.existsByEmpleadoIdAndPeriodo(3L, "2026-06")).thenReturn(false);
        when(nominaRepository.save(any(Nomina.class))).thenAnswer(invocation -> {
            Nomina nomina = invocation.getArgument(0);
            nomina.setId(1L);
            return nomina;
        });

        NominaResponse response = nominaService.crear(request);

        assertThat(response.sueldoLiquido()).isEqualByComparingTo("1255000");
        assertThat(response.estadoPago()).isEqualTo(EstadoPago.PENDIENTE);
    }

    @Test
    void crearDebeFallarCuandoPeriodoYaExiste() {
        CrearNominaRequest request = new CrearNominaRequest(
                3L,
                "2026-06",
                new BigDecimal("1200000"),
                BigDecimal.ZERO,
                BigDecimal.ZERO
        );

        when(nominaRepository.existsByEmpleadoIdAndPeriodo(3L, "2026-06")).thenReturn(true);

        assertThatThrownBy(() -> nominaService.crear(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Ya existe una nomina para ese empleado en el periodo indicado");
    }

    @Test
    void pagarDebeCambiarEstadoYFechaPago() {
        Nomina nomina = new Nomina(
                8L,
                3L,
                "2026-06",
                new BigDecimal("1200000"),
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                new BigDecimal("1200000"),
                EstadoPago.PENDIENTE,
                null
        );

        when(nominaRepository.findById(8L)).thenReturn(Optional.of(nomina));
        when(nominaRepository.save(any(Nomina.class))).thenAnswer(invocation -> invocation.getArgument(0));

        NominaResponse response = nominaService.pagar(8L);

        assertThat(response.estadoPago()).isEqualTo(EstadoPago.PAGADA);
        assertThat(response.fechaPago()).isEqualTo(LocalDate.now());
    }

    @Test
    void crearDebeFallarSiElLiquidoEsNegativo() {
        CrearNominaRequest request = new CrearNominaRequest(
                3L,
                "2026-06",
                new BigDecimal("100000"),
                BigDecimal.ZERO,
                new BigDecimal("150000")
        );

        when(nominaRepository.existsByEmpleadoIdAndPeriodo(3L, "2026-06")).thenReturn(false);

        assertThatThrownBy(() -> nominaService.crear(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("El sueldo liquido no puede ser negativo");
    }
}
