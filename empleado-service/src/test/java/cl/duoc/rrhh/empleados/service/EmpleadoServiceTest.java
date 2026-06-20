package cl.duoc.rrhh.empleados.service;

import cl.duoc.rrhh.empleados.dto.request.ActualizarEmpleadoRequest;
import cl.duoc.rrhh.empleados.dto.request.CrearEmpleadoRequest;
import cl.duoc.rrhh.empleados.dto.response.EmpleadoResponse;
import cl.duoc.rrhh.empleados.entity.Empleado;
import cl.duoc.rrhh.empleados.entity.EstadoEmpleado;
import cl.duoc.rrhh.empleados.exception.DuplicateResourceException;
import cl.duoc.rrhh.empleados.repository.EmpleadoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class EmpleadoServiceTest {

    @Mock
    private EmpleadoRepository empleadoRepository;

    @InjectMocks
    private EmpleadoService empleadoService;

    @Test
    void crearDebeGuardarEmpleadoActivo() {
        CrearEmpleadoRequest request = new CrearEmpleadoRequest(
                "12.345.678-9",
                "Ana Torres",
                "Analista",
                "RRHH",
                "ana@empresa.cl",
                LocalDate.of(2024, 3, 1)
        );

        when(empleadoRepository.existsByRutIgnoreCase(request.rut())).thenReturn(false);
        when(empleadoRepository.existsByEmailIgnoreCase(request.email())).thenReturn(false);
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> {
            Empleado empleado = invocation.getArgument(0);
            empleado.setId(1L);
            return empleado;
        });

        EmpleadoResponse response = empleadoService.crear(request);

        ArgumentCaptor<Empleado> captor = ArgumentCaptor.forClass(Empleado.class);
        verify(empleadoRepository).save(captor.capture());
        assertThat(captor.getValue().getEstado()).isEqualTo(EstadoEmpleado.ACTIVO);
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    void actualizarDebeFallarSiElEmailYaExiste() {
        ActualizarEmpleadoRequest request = new ActualizarEmpleadoRequest(
                "12.345.678-9",
                "Ana Torres",
                "Analista Senior",
                "RRHH",
                "duplicado@empresa.cl",
                LocalDate.of(2024, 3, 1)
        );

        when(empleadoRepository.findById(7L)).thenReturn(Optional.of(new Empleado()));
        when(empleadoRepository.existsByRutIgnoreCaseAndIdNot(request.rut(), 7L)).thenReturn(false);
        when(empleadoRepository.existsByEmailIgnoreCaseAndIdNot(request.email(), 7L)).thenReturn(true);

        assertThatThrownBy(() -> empleadoService.actualizar(7L, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("El email ya se encuentra registrado");
    }

    @Test
    void desactivarDebeCambiarEstadoAInactivo() {
        Empleado empleado = new Empleado(
                2L,
                "11.111.111-1",
                "Luis Soto",
                "Coordinador",
                "Capacitacion",
                "luis@empresa.cl",
                LocalDate.of(2023, 1, 5),
                EstadoEmpleado.ACTIVO
        );

        when(empleadoRepository.findById(2L)).thenReturn(Optional.of(empleado));
        when(empleadoRepository.save(any(Empleado.class))).thenAnswer(invocation -> invocation.getArgument(0));

        EmpleadoResponse response = empleadoService.desactivar(2L);

        assertThat(response.estado()).isEqualTo(EstadoEmpleado.INACTIVO);
    }
}
