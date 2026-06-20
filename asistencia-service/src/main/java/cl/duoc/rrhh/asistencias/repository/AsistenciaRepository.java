package cl.duoc.rrhh.asistencias.repository;

import cl.duoc.rrhh.asistencias.entity.Asistencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AsistenciaRepository extends JpaRepository<Asistencia, Long> {

    boolean existsByEmpleadoIdAndFecha(Long empleadoId, LocalDate fecha);

    boolean existsByEmpleadoIdAndFechaAndIdNot(Long empleadoId, LocalDate fecha, Long id);

    List<Asistencia> findAllByEmpleadoIdOrderByFechaDesc(Long empleadoId);
}
