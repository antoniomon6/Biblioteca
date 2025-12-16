package com.amalagonj.biblioteca.repositories;

import com.amalagonj.biblioteca.entidades.Prestamo;
import com.amalagonj.biblioteca.entidades.Socio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
    long countBySocioAndFechaDevolucionIsNull(Socio socio);
}
