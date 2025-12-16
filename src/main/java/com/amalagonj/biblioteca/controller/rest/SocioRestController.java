package com.amalagonj.biblioteca.controller.rest;

import com.amalagonj.biblioteca.entidades.Socio;
import com.amalagonj.biblioteca.services.SocioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socios")
@Tag(name = "Socios", description = "API para la gestión de socios")
public class SocioRestController {

    @Autowired
    private SocioService socioService;

    @Operation(summary = "Listar todos los socios")
    @GetMapping
    public ResponseEntity<List<Socio>> listarSocios() {
        return ResponseEntity.ok(socioService.findAll());
    }

    @Operation(summary = "Obtener un socio por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Socio encontrado"),
            @ApiResponse(responseCode = "404", description = "Socio no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Socio> obtenerSocio(@PathVariable Long id) {
        Socio socio = socioService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Socio no encontrado con ID: " + id));
        return ResponseEntity.ok(socio);
    }

    @Operation(summary = "Crear un nuevo socio")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Socio creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Socio> crearSocio(@Valid @RequestBody Socio socio) {
        Socio nuevoSocio = socioService.save(socio);
        return new ResponseEntity<>(nuevoSocio, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un socio existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Socio actualizado"),
            @ApiResponse(responseCode = "404", description = "Socio no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Socio> actualizarSocio(@PathVariable Long id, @Valid @RequestBody Socio socio) {
        return socioService.findById(id)
                .map(socioExistente -> {
                    socio.setSocioId(id);
                    Socio socioActualizado = socioService.save(socio);
                    return ResponseEntity.ok(socioActualizado);
                })
                .orElseThrow(() -> new EntityNotFoundException("Socio no encontrado con ID: " + id));
    }

    @Operation(summary = "Eliminar un socio por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Socio eliminado"),
            @ApiResponse(responseCode = "404", description = "Socio no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto de integridad de datos (el socio tiene préstamos)", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSocio(@PathVariable Long id) {
        if (!socioService.findById(id).isPresent()) {
            throw new EntityNotFoundException("Socio no encontrado con ID: " + id);
        }
        socioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
