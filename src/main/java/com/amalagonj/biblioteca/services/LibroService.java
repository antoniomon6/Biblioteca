package com.amalagonj.biblioteca.services;

import com.amalagonj.biblioteca.entidades.Libro;

import java.util.List;
import java.util.Optional;

public interface LibroService {
    Libro save(Libro libro);
    void deleteById(Long id);
    Optional<Libro> findById(Long id);
    List<Libro> findAll();
    List<Libro> findByTituloContaining(String titulo);
}
