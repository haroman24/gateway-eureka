package cl.duoc.rrhh.empleados.service;

import cl.duoc.rrhh.empleados.dto.request.ActualizarEmpleadoRequest;
import cl.duoc.rrhh.empleados.dto.request.CrearEmpleadoRequest;
import cl.duoc.rrhh.empleados.dto.response.EmpleadoResponse;
import cl.duoc.rrhh.empleados.entity.Empleado;
import cl.duoc.rrhh.empleados.entity.EstadoEmpleado;
import cl.duoc.rrhh.empleados.exception.BusinessRuleException;
import cl.duoc.rrhh.empleados.exception.DuplicateResourceException;
import cl.duoc.rrhh.empleados.exception.ResourceNotFoundException;
import cl.duoc.rrhh.empleados.repository.EmpleadoRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class EmpleadoService {

    private final EmpleadoRepository empleadoRepository;

    public EmpleadoService(EmpleadoRepository empleadoRepository) {
        this.empleadoRepository = empleadoRepository;
    }

    @Transactional
    public EmpleadoResponse crear(CrearEmpleadoRequest request) {
        validarDuplicados(request.rut(), request.email(), null);
        Empleado empleado = new Empleado(
                null,
                request.rut(),
                request.nombre(),
                request.cargo(),
                request.departamento(),
                request.email(),
                request.fechaIngreso(),
                EstadoEmpleado.ACTIVO
        );
        return toResponse(empleadoRepository.save(empleado));
    }

    @Transactional(readOnly = true)
    public List<EmpleadoResponse> listar(String estado, String departamento) {
        EstadoEmpleado estadoBuscado = parseEstado(estado);
        List<Empleado> empleados;
        if (estadoBuscado != null) {
            empleados = empleadoRepository.findAllByEstadoOrderByIdAsc(estadoBuscado);
        } else if (departamento != null && !departamento.isBlank()) {
            empleados = empleadoRepository.findAllByDepartamentoIgnoreCaseOrderByIdAsc(departamento);
        } else {
            empleados = empleadoRepository.findAll(Sort.by(Sort.Order.asc("id")));
        }

        if (departamento != null && !departamento.isBlank() && estadoBuscado != null) {
            String departamentoBuscado = departamento.trim().toLowerCase();
            empleados = empleados.stream()
                    .filter(empleado -> empleado.getEstado() == estadoBuscado)
                    .filter(empleado -> empleado.getDepartamento().trim().toLowerCase().equals(departamentoBuscado))
                    .toList();
        }

        return empleados.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public EmpleadoResponse buscarPorId(Long id) {
        return toResponse(obtenerEntidad(id));
    }

    @Transactional
    public EmpleadoResponse actualizar(Long id, ActualizarEmpleadoRequest request) {
        Empleado empleado = obtenerEntidad(id);
        validarDuplicados(request.rut(), request.email(), id);
        empleado.setRut(request.rut());
        empleado.setNombre(request.nombre());
        empleado.setCargo(request.cargo());
        empleado.setDepartamento(request.departamento());
        empleado.setEmail(request.email());
        empleado.setFechaIngreso(request.fechaIngreso());
        return toResponse(empleadoRepository.save(empleado));
    }

    @Transactional
    public EmpleadoResponse desactivar(Long id) {
        Empleado empleado = obtenerEntidad(id);
        empleado.setEstado(EstadoEmpleado.INACTIVO);
        return toResponse(empleadoRepository.save(empleado));
    }

    private Empleado obtenerEntidad(Long id) {
        return empleadoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe un empleado con id " + id));
    }

    private void validarDuplicados(String rut, String email, Long idActual) {
        boolean rutDuplicado = idActual == null
                ? empleadoRepository.existsByRutIgnoreCase(rut)
                : empleadoRepository.existsByRutIgnoreCaseAndIdNot(rut, idActual);
        if (rutDuplicado) {
            throw new DuplicateResourceException("El rut ya se encuentra registrado");
        }

        boolean emailDuplicado = idActual == null
                ? empleadoRepository.existsByEmailIgnoreCase(email)
                : empleadoRepository.existsByEmailIgnoreCaseAndIdNot(email, idActual);
        if (emailDuplicado) {
            throw new DuplicateResourceException("El email ya se encuentra registrado");
        }
    }

    private EstadoEmpleado parseEstado(String estado) {
        if (estado == null || estado.isBlank()) {
            return null;
        }
        try {
            return EstadoEmpleado.valueOf(estado.trim().toUpperCase());
        } catch (IllegalArgumentException exception) {
            throw new BusinessRuleException("El estado debe ser ACTIVO o INACTIVO");
        }
    }

    private EmpleadoResponse toResponse(Empleado empleado) {
        return new EmpleadoResponse(
                empleado.getId(),
                empleado.getRut(),
                empleado.getNombre(),
                empleado.getCargo(),
                empleado.getDepartamento(),
                empleado.getEmail(),
                empleado.getFechaIngreso(),
                empleado.getEstado()
        );
    }
}
