package com.amalagonj.biblioteca.controller.rest;

import com.amalagonj.biblioteca.entidades.Libro;
import com.amalagonj.biblioteca.services.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
@RequestMapping("/api/libros")
@Tag(name = "Libros", description = "API para la gestión de libros")
public class LibroRestController {

    @Autowired
    private LibroService libroService;

    @Operation(summary = "Listar todos los libros")
    @GetMapping
    public ResponseEntity<List<Libro>> listarLibros() {
        return ResponseEntity.ok(libroService.findAll());
    }

    @Operation(summary = "Obtener un libro por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro encontrado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerLibro(@PathVariable Long id) {
        Libro libro = libroService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Libro no encontrado con ID: " + id));
        return ResponseEntity.ok(libro);
    }

    @Operation(summary = "Crear un nuevo libro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Libro creado exitosamente"),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Libro> crearLibro(@Valid @RequestBody Libro libro) {
        Libro nuevoLibro = libroService.save(libro);
        return new ResponseEntity<>(nuevoLibro, HttpStatus.CREATED);
    }

    @Operation(summary = "Actualizar un libro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Libro actualizado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content),
            @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizarLibro(@PathVariable Long id, @Valid @RequestBody Libro libro) {
        return libroService.findById(id)
                .map(libroExistente -> {
                    libro.setLibroId(id);
                    Libro libroActualizado = libroService.save(libro);
                    return ResponseEntity.ok(libroActualizado);
                })
                .orElseThrow(() -> new EntityNotFoundException("Libro no encontrado con ID: " + id));
    }

    @Operation(summary = "Eliminar un libro por su ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Libro eliminado"),
            @ApiResponse(responseCode = "404", description = "Libro no encontrado", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflicto de integridad de datos (el libro está en uso)", content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarLibro(@PathVariable Long id) {
        if (!libroService.findById(id).isPresent()) {
            throw new EntityNotFoundException("Libro no encontrado con ID: " + id);
        }
        libroService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
