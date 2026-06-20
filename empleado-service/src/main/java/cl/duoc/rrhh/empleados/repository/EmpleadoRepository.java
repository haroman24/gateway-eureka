package cl.duoc.rrhh.empleados.repository;

import cl.duoc.rrhh.empleados.entity.Empleado;
import cl.duoc.rrhh.empleados.entity.EstadoEmpleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {

    boolean existsByRutIgnoreCase(String rut);

    boolean existsByEmailIgnoreCase(String email);

    boolean existsByRutIgnoreCaseAndIdNot(String rut, Long id);

    boolean existsByEmailIgnoreCaseAndIdNot(String email, Long id);

    List<Empleado> findAllByEstadoOrderByIdAsc(EstadoEmpleado estado);

    List<Empleado> findAllByDepartamentoIgnoreCaseOrderByIdAsc(String departamento);
}
