package cl.duoc.rrhh.empleados.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record ActualizarEmpleadoRequest(
        @NotBlank(message = "El rut es obligatorio")
        @Size(max = 20, message = "El rut no puede superar los 20 caracteres")
        String rut,

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
        String nombre,

        @NotBlank(message = "El cargo es obligatorio")
        @Size(max = 80, message = "El cargo no puede superar los 80 caracteres")
        String cargo,

        @NotBlank(message = "El departamento es obligatorio")
        @Size(max = 80, message = "El departamento no puede superar los 80 caracteres")
        String departamento,

        @Email(message = "El email no tiene un formato valido")
        @NotBlank(message = "El email es obligatorio")
        @Size(max = 120, message = "El email no puede superar los 120 caracteres")
        String email,

        @NotNull(message = "La fecha de ingreso es obligatoria")
        @PastOrPresent(message = "La fecha de ingreso no puede estar en el futuro")
        LocalDate fechaIngreso
) {
}
