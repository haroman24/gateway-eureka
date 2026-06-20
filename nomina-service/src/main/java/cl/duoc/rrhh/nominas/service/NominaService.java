package cl.duoc.rrhh.nominas.service;

import cl.duoc.rrhh.nominas.dto.request.CrearNominaRequest;
import cl.duoc.rrhh.nominas.dto.response.NominaResponse;
import cl.duoc.rrhh.nominas.entity.EstadoPago;
import cl.duoc.rrhh.nominas.entity.Nomina;
import cl.duoc.rrhh.nominas.exception.BusinessRuleException;
import cl.duoc.rrhh.nominas.exception.DuplicateResourceException;
import cl.duoc.rrhh.nominas.exception.ResourceNotFoundException;
import cl.duoc.rrhh.nominas.repository.NominaRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
public class NominaService {

    private final NominaRepository nominaRepository;

    public NominaService(NominaRepository nominaRepository) {
        this.nominaRepository = nominaRepository;
    }

    @Transactional
    public NominaResponse crear(CrearNominaRequest request) {
        if (nominaRepository.existsByEmpleadoIdAndPeriodo(request.empleadoId(), request.periodo())) {
            throw new DuplicateResourceException("Ya existe una nomina para ese empleado en el periodo indicado");
        }

        BigDecimal sueldoLiquido = request.sueldoBase()
                .add(request.bono())
                .subtract(request.descuento());
        if (sueldoLiquido.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessRuleException("El sueldo liquido no puede ser negativo");
        }

        Nomina nomina = new Nomina(
                null,
                request.empleadoId(),
                request.periodo(),
                request.sueldoBase(),
                request.bono(),
                request.descuento(),
                sueldoLiquido,
                EstadoPago.PENDIENTE,
                null
        );
        return toResponse(nominaRepository.save(nomina));
    }

    @Transactional(readOnly = true)
    public List<NominaResponse> listar(Long empleadoId) {
        List<Nomina> nominas = empleadoId == null
                ? nominaRepository.findAll(Sort.by(Sort.Order.desc("periodo"), Sort.Order.asc("id")))
                : nominaRepository.findAllByEmpleadoIdOrderByPeriodoDesc(empleadoId);
        return nominas.stream().map(this::toResponse).toList();
    }

    @Transactional(readOnly = true)
    public NominaResponse buscarPorId(Long id) {
        return toResponse(obtenerEntidad(id));
    }

    @Transactional
    public NominaResponse pagar(Long id) {
        Nomina nomina = obtenerEntidad(id);
        if (nomina.getEstadoPago() == EstadoPago.PAGADA) {
            throw new BusinessRuleException("La nomina ya fue marcada como pagada");
        }
        nomina.setEstadoPago(EstadoPago.PAGADA);
        nomina.setFechaPago(LocalDate.now());
        return toResponse(nominaRepository.save(nomina));
    }

    private Nomina obtenerEntidad(Long id) {
        return nominaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No existe una nomina con id " + id));
    }

    private NominaResponse toResponse(Nomina nomina) {
        return new NominaResponse(
                nomina.getId(),
                nomina.getEmpleadoId(),
                nomina.getPeriodo(),
                nomina.getSueldoBase(),
                nomina.getBono(),
                nomina.getDescuento(),
                nomina.getSueldoLiquido(),
                nomina.getEstadoPago(),
                nomina.getFechaPago()
        );
    }
}
