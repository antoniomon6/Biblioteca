package com.amalagonj.biblioteca.controller.rest;

import com.amalagonj.biblioteca.entidades.Socio;
import com.amalagonj.biblioteca.services.SocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/socios")
public class SocioRestController {

    @Autowired
    private SocioService socioService;

    @GetMapping
    public ResponseEntity<List<Socio>> listarSocios() {

        return ResponseEntity.ok(socioService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Socio> obtenerSocio(@PathVariable Long id) {
        return socioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Socio> crearSocio(@RequestBody Socio socio) {
        Socio nuevoSocio = socioService.save(socio);
        return new ResponseEntity<>(nuevoSocio, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Socio> actualizarSocio(@PathVariable Long id,
                                                 @RequestBody Socio socio) {
        return socioService.findById(id)
                .map(socioExistente -> {
                    socio.setSocioId(id);
                    Socio socioActualizado = socioService.save(socio);
                    return ResponseEntity.ok(socioActualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSocio(@PathVariable Long id) {
        socioService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
