package cl.duoc.rrhh.asistencias.dto.request;

import cl.duoc.rrhh.asistencias.entity.EstadoAsistencia;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.LocalTime;

public record CrearAsistenciaRequest(
        @NotNull(message = "El empleadoId es obligatorio")
        Long empleadoId,

        @NotNull(message = "La fecha es obligatoria")
        LocalDate fecha,

        LocalTime horaEntrada,

        LocalTime horaSalida,

        @NotNull(message = "El estado es obligatorio")
        EstadoAsistencia estado,

        @Size(max = 250, message = "La observacion no puede superar los 250 caracteres")
        String observacion
) {
}
