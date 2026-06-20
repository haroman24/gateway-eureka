package cl.duoc.rrhh.asistencias.controller;

import cl.duoc.rrhh.asistencias.dto.request.ActualizarAsistenciaRequest;
import cl.duoc.rrhh.asistencias.dto.request.CrearAsistenciaRequest;
import cl.duoc.rrhh.asistencias.dto.response.AsistenciaResponse;
import cl.duoc.rrhh.asistencias.service.AsistenciaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    private final AsistenciaService asistenciaService;

    public AsistenciaController(AsistenciaService asistenciaService) {
        this.asistenciaService = asistenciaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AsistenciaResponse crear(@Valid @RequestBody CrearAsistenciaRequest request) {
        return asistenciaService.crear(request);
    }

    @GetMapping
    public List<AsistenciaResponse> listar(@RequestParam(required = false) Long empleadoId) {
        return asistenciaService.listar(empleadoId);
    }

    @GetMapping("/{id}")
    public AsistenciaResponse buscarPorId(@PathVariable Long id) {
        return asistenciaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public AsistenciaResponse actualizar(@PathVariable Long id, @Valid @RequestBody ActualizarAsistenciaRequest request) {
        return asistenciaService.actualizar(id, request);
    }
}
