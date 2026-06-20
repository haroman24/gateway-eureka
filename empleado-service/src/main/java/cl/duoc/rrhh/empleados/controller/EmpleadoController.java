package cl.duoc.rrhh.empleados.controller;

import cl.duoc.rrhh.empleados.dto.request.ActualizarEmpleadoRequest;
import cl.duoc.rrhh.empleados.dto.request.CrearEmpleadoRequest;
import cl.duoc.rrhh.empleados.dto.response.EmpleadoResponse;
import cl.duoc.rrhh.empleados.service.EmpleadoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/empleados")
public class EmpleadoController {

    private final EmpleadoService empleadoService;

    public EmpleadoController(EmpleadoService empleadoService) {
        this.empleadoService = empleadoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmpleadoResponse crear(@Valid @RequestBody CrearEmpleadoRequest request) {
        return empleadoService.crear(request);
    }

    @GetMapping
    public List<EmpleadoResponse> listar(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String departamento) {
        return empleadoService.listar(estado, departamento);
    }

    @GetMapping("/{id}")
    public EmpleadoResponse buscarPorId(@PathVariable Long id) {
        return empleadoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public EmpleadoResponse actualizar(@PathVariable Long id, @Valid @RequestBody ActualizarEmpleadoRequest request) {
        return empleadoService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public EmpleadoResponse desactivar(@PathVariable Long id) {
        return empleadoService.desactivar(id);
    }
}
