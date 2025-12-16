package com.amalagonj.biblioteca.services;

import com.amalagonj.biblioteca.entidades.Socio;
import com.amalagonj.biblioteca.repositories.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SocioServiceImpl implements SocioService {

    @Autowired
    private SocioRepository socioRepository;

    @Override
    public Socio save(Socio socio) {
        return socioRepository.save(socio);
    }

    @Override
    public void deleteById(Long id) {
        socioRepository.deleteById(id);
    }

    @Override
    public Optional<Socio> findById(Long id) {
        return socioRepository.findById(id);
    }

    @Override
    public List<Socio> findAll() {
        return socioRepository.findAll();
    }

    @Override
    public List<Socio> findByNombreContaining(String nombre) {
        return socioRepository.findByNombreContaining(nombre);
    }
}
