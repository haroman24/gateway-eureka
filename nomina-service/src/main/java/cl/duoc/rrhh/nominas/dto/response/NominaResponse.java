package cl.duoc.rrhh.nominas.dto.response;

import cl.duoc.rrhh.nominas.entity.EstadoPago;

import java.math.BigDecimal;
import java.time.LocalDate;

public record NominaResponse(
        Long id,
        Long empleadoId,
        String periodo,
        BigDecimal sueldoBase,
        BigDecimal bono,
        BigDecimal descuento,
        BigDecimal sueldoLiquido,
        EstadoPago estadoPago,
        LocalDate fechaPago
) {
}
