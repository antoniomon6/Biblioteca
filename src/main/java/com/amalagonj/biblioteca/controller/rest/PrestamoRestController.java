package com.amalagonj.biblioteca.controller.rest;

import com.amalagonj.biblioteca.entidades.Prestamo;
import com.amalagonj.biblioteca.services.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/prestamos")
public class PrestamoRestController {

    @Autowired
    private PrestamoService prestamoService;

    @GetMapping
    public ResponseEntity<List<Prestamo>> listarPrestamos() {
        return ResponseEntity.ok(prestamoService.listarTodosLosPrestamos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prestamo> obtenerPrestamo(@PathVariable Long id) {
        return prestamoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Prestamo> crearPrestamo(@RequestBody Prestamo prestamo) {
        Prestamo nuevoPrestamo = prestamoService.guardarPrestamo(prestamo);
        return new ResponseEntity<>(nuevoPrestamo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/devolver")
    public ResponseEntity<Void> devolverPrestamo(@PathVariable Long id) {
        prestamoService.devolverPrestamo(id);
        return ResponseEntity.ok().build();
    }
}
