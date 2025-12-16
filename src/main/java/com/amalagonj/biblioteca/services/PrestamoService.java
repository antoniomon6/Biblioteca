package com.amalagonj.biblioteca.services;

import com.amalagonj.biblioteca.entidades.Prestamo;

import java.util.List;

public interface PrestamoService {
    List<Prestamo> listarTodosLosPrestamos();
    Prestamo guardarPrestamo(Prestamo prestamo);
    void devolverPrestamo(Long prestamoId);
    void borrarPrestamo(Long id);
}
