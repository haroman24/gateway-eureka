package cl.duoc.rrhh.nominas.controller;

import cl.duoc.rrhh.nominas.dto.request.CrearNominaRequest;
import cl.duoc.rrhh.nominas.dto.response.NominaResponse;
import cl.duoc.rrhh.nominas.service.NominaService;
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
@RequestMapping("/api/nominas")
public class NominaController {

    private final NominaService nominaService;

    public NominaController(NominaService nominaService) {
        this.nominaService = nominaService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NominaResponse crear(@Valid @RequestBody CrearNominaRequest request) {
        return nominaService.crear(request);
    }

    @GetMapping
    public List<NominaResponse> listar(@RequestParam(required = false) Long empleadoId) {
        return nominaService.listar(empleadoId);
    }

    @GetMapping("/{id}")
    public NominaResponse buscarPorId(@PathVariable Long id) {
        return nominaService.buscarPorId(id);
    }

    @PutMapping("/{id}/pagar")
    public NominaResponse pagar(@PathVariable Long id) {
        return nominaService.pagar(id);
    }
}
