package cl.duoc.rrhh.asistencias.dto.response;

import cl.duoc.rrhh.asistencias.entity.EstadoAsistencia;

import java.time.LocalDate;
import java.time.LocalTime;

public record AsistenciaResponse(
        Long id,
        Long empleadoId,
        LocalDate fecha,
        LocalTime horaEntrada,
        LocalTime horaSalida,
        EstadoAsistencia estado,
        String observacion
) {
}
