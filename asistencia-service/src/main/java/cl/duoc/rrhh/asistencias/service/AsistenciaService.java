package cl.duoc.rrhh.asistencias.service;

import cl.duoc.rrhh.asistencias.dto.request.ActualizarAsistenciaRequest;
import cl.duoc.rrhh.asistencias.dto.request.CrearAsistenciaRequest;
import cl.duoc.rrhh.asistencias.dto.response.AsistenciaResponse;
import cl.duoc.rrhh.asistencias.entity.Asistencia;
import cl.duoc.rrhh.asistencias.exception.BusinessRuleException;
import cl.duoc.rrhh.asistencias.exception.DuplicateResourceException;
import cl.duoc.rrhh.asistencias.exception.ResourceNotFoundException;
import cl.duoc.rrhh.asistencias.repository.AsistenciaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AsistenciaService {

    private final AsistenciaRepository asistenciaRepository;

    public AsistenciaService(AsistenciaRepository asistenciaRepository) {
        this.asistenciaRepository = asistenciaRepository;
    }

    @Transactional
    public AsistenciaResponse crear(CrearAsistenciaRequest request) {
        validarHorario(request.horaEntrada(), request.horaSalida());
        if (asistenciaRepository.existsByEmpleadoIdAndFecha(request.empleadoId(), request.fecha())) {
            throw new DuplicateResourceException("Ya existe una asistencia para ese empleado en la fecha indicada");
        }

        Asistencia asistencia = new Asistencia(
                null,
                request.empleadoId(),
                request.fecha(),
                request.horaEntrada(),
                request.horaSalida(),
                request.estado(),
                request.observacion()
        );
        return toResponse(asistenciaRepository.save(asistencia));
    }

    @Transactional(readOnly = true)
    public List<AsistenciaResponse> listar(Long empleadoId) {
        List<Asistencia> asistencias = empleadoId == null
                ? asistenciaRepository.findAll(Sort.by(Sort.Order.desc("fecha"), Sort.Order.asc("id")))
                : asistenciaRepository.findAllByEmpleadoIdOrderByFechaDesc(empleadoId);
        return asistencias.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public AsistenciaResponse buscarPorId(Long id) {
        return toResponse(obtenerEntidad(id));
    }

    @Transactional
    public AsistenciaResponse actualizar(Long id, ActualizarAsistenciaRequest request) {
        Asistencia asistencia = obtenerEntidad(id);
        validarHorario(request.horaEntrada(), request.horaSalida());
        if (asistenciaRepository.existsByEmpleadoIdAndFechaAndIdNot(request.empleadoId(), request.fecha(), id)) {
            throw new DuplicateResourceException("Ya existe una asistencia para ese empleado en la fecha indicada");
        }

        asistencia.setEmpleadoId(request.empleadoId());
        asistencia.setFecha(request.fecha());
        asistencia.setHoraEntrada(request.horaEntrada());
        asistencia.setHoraSalida(request.horaSalida());
        asistencia.setEstado(request.estado());
        asistencia.setObservacion(request.observacion());
        return toResponse(asistenciaRepository.save(asistencia));
    }

    private Asistencia obtenerEntidad(Long id) {
        return asistenciaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una asistencia con id " + id));
    }

    private void validarHorario(java.time.LocalTime horaEntrada, java.time.LocalTime horaSalida) {
        if (horaEntrada != null && horaSalida != null && horaSalida.isBefore(horaEntrada)) {
            throw new BusinessRuleException("La hora de salida no puede ser anterior a la hora de entrada");
        }
    }

    private AsistenciaResponse toResponse(Asistencia asistencia) {
        return new AsistenciaResponse(
                asistencia.getId(),
                asistencia.getEmpleadoId(),
                asistencia.getFecha(),
                asistencia.getHoraEntrada(),
                asistencia.getHoraSalida(),
                asistencia.getEstado(),
                asistencia.getObservacion()
        );
    }
}
