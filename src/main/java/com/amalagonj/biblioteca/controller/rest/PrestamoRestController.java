package com.amalagonj.biblioteca.controller.rest;

import com.amalagonj.biblioteca.entidades.Prestamo;
import com.amalagonj.biblioteca.services.PrestamoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
@Tag(name = "Préstamos", description = "API para la gestión de préstamos")
public class PrestamoRestController {

    @Autowired
    private PrestamoService prestamoService;

    @Operation(summary = "Listar todos los préstamos")
    @GetMapping
    public ResponseEntity<List<Prestamo>> listarPrestamos() {
        return ResponseEntity.ok(prestamoService.listarTodosLosPrestamos());
    }

    @Operation(summary = "Obtener un préstamo por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Préstamo encontrado"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> obtenerPrestamo(@PathVariable Long id) {
        Prestamo prestamo = prestamoService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Préstamo no encontrado con ID: " + id));
        return ResponseEntity.ok(prestamo);
    }

    @Operation(summary = "Crear un nuevo préstamo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Préstamo creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Socio o libro no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto por regla de negocio (ej: socio sancionado, límite de préstamos)", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Prestamo> crearPrestamo(@RequestBody Prestamo prestamo) {
        Prestamo nuevoPrestamo = prestamoService.guardarPrestamo(prestamo);
        return new ResponseEntity<>(nuevoPrestamo, HttpStatus.CREATED);
    }

    @Operation(summary = "Registrar la devolución de un préstamo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devolución registrada"),
            @ApiResponse(responseCode = "404", description = "Préstamo no encontrado", content = @Content)
    })
    @PutMapping("/{id}/devolver")
    public ResponseEntity<Void> devolverPrestamo(@PathVariable Long id) {
        prestamoService.devolverPrestamo(id);
        return ResponseEntity.ok().build();
    }
}
