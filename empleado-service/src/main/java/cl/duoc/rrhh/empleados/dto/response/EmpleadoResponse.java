package cl.duoc.rrhh.empleados.dto.response;

import cl.duoc.rrhh.empleados.entity.EstadoEmpleado;

import java.time.LocalDate;

public record EmpleadoResponse(
        Long id,
        String rut,
        String nombre,
        String cargo,
        String departamento,
        String email,
        LocalDate fechaIngreso,
        EstadoEmpleado estado
) {
}
