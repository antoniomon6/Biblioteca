package com.amalagonj.biblioteca.services;

import com.amalagonj.biblioteca.entidades.Socio;

import java.util.List;
import java.util.Optional;

public interface SocioService {
    Socio save(Socio socio);
    void deleteById(Long id);
    Optional<Socio> findById(Long id);
    List<Socio> findAll();
    List<Socio> findByNombreContaining(String nombre);
}
