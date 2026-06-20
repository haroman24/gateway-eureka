package cl.duoc.rrhh.asistencias.service;

import cl.duoc.rrhh.asistencias.dto.request.CrearAsistenciaRequest;
import cl.duoc.rrhh.asistencias.dto.response.AsistenciaResponse;
import cl.duoc.rrhh.asistencias.entity.Asistencia;
import cl.duoc.rrhh.asistencias.entity.EstadoAsistencia;
import cl.duoc.rrhh.asistencias.exception.BusinessRuleException;
import cl.duoc.rrhh.asistencias.exception.DuplicateResourceException;
import cl.duoc.rrhh.asistencias.repository.AsistenciaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AsistenciaServiceTest {

    @Mock
    private AsistenciaRepository asistenciaRepository;

    @InjectMocks
    private AsistenciaService asistenciaService;

    @Test
    void crearDebePersistirAsistenciaValida() {
        CrearAsistenciaRequest request = new CrearAsistenciaRequest(
                4L,
                LocalDate.of(2026, 6, 19),
                LocalTime.of(8, 30),
                LocalTime.of(17, 45),
                EstadoAsistencia.PRESENTE,
                "Turno regular"
        );

        when(asistenciaRepository.existsByEmpleadoIdAndFecha(4L, LocalDate.of(2026, 6, 19))).thenReturn(false);
        when(asistenciaRepository.save(any(Asistencia.class))).thenAnswer(invocation -> {
            Asistencia asistencia = invocation.getArgument(0);
            asistencia.setId(1L);
            return asistencia;
        });

        AsistenciaResponse response = asistenciaService.crear(request);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.estado()).isEqualTo(EstadoAsistencia.PRESENTE);
    }

    @Test
    void crearDebeFallarCuandoYaExisteRegistroParaLaFecha() {
        CrearAsistenciaRequest request = new CrearAsistenciaRequest(
                9L,
                LocalDate.of(2026, 6, 19),
                LocalTime.of(8, 30),
                LocalTime.of(17, 45),
                EstadoAsistencia.PRESENTE,
                null
        );

        when(asistenciaRepository.existsByEmpleadoIdAndFecha(9L, LocalDate.of(2026, 6, 19))).thenReturn(true);

        assertThatThrownBy(() -> asistenciaService.crear(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("Ya existe una asistencia para ese empleado en la fecha indicada");
    }

    @Test
    void crearDebeFallarSiLaHoraDeSalidaEsAnterior() {
        CrearAsistenciaRequest request = new CrearAsistenciaRequest(
                9L,
                LocalDate.of(2026, 6, 19),
                LocalTime.of(18, 0),
                LocalTime.of(8, 0),
                EstadoAsistencia.TARDANZA,
                null
        );

        assertThatThrownBy(() -> asistenciaService.crear(request))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessage("La hora de salida no puede ser anterior a la hora de entrada");
    }
}
