package cl.duoc.rrhh.nominas.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record CrearNominaRequest(
        @NotNull(message = "El empleadoId es obligatorio")
        Long empleadoId,

        @NotBlank(message = "El periodo es obligatorio")
        @Pattern(regexp = "^\\d{4}-(0[1-9]|1[0-2])$", message = "El periodo debe tener formato yyyy-MM")
        String periodo,

        @NotNull(message = "El sueldo base es obligatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "El sueldo base no puede ser negativo")
        BigDecimal sueldoBase,

        @NotNull(message = "El bono es obligatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "El bono no puede ser negativo")
        BigDecimal bono,

        @NotNull(message = "El descuento es obligatorio")
        @DecimalMin(value = "0.0", inclusive = true, message = "El descuento no puede ser negativo")
        BigDecimal descuento
) {
}
