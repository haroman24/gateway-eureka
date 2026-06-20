package cl.duoc.rrhh.nominas.repository;

import cl.duoc.rrhh.nominas.entity.Nomina;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NominaRepository extends JpaRepository<Nomina, Long> {

    boolean existsByEmpleadoIdAndPeriodo(Long empleadoId, String periodo);

    List<Nomina> findAllByEmpleadoIdOrderByPeriodoDesc(Long empleadoId);
}
