package com.amalagonj.biblioteca.services;

import com.amalagonj.biblioteca.entidades.Prestamo;
import com.amalagonj.biblioteca.entidades.Socio;
import com.amalagonj.biblioteca.excepciones.ReglaNegocioException;
import com.amalagonj.biblioteca.repositories.PrestamoRepository;
import com.amalagonj.biblioteca.repositories.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PrestamoServiceImpl implements PrestamoService {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private SocioRepository socioRepository;

    @Override
    public List<Prestamo> listarTodosLosPrestamos() {
        return prestamoRepository.findAll();
    }

    @Override
    public Prestamo guardarPrestamo(Prestamo prestamo) {
        // Validación 1: Socio sancionado
        if (prestamo.getSocio().getEstado() == Socio.EstadoSocio.SANCIONADO) {
            if (prestamo.getSocio().getFechaFinPenalizacion() != null && prestamo.getSocio().getFechaFinPenalizacion().isAfter(LocalDate.now())) {
                throw new ReglaNegocioException("El socio está sancionado y no puede realizar préstamos.");
            } else {
                // Si la fecha de penalización ha pasado, se quita la sanción
                Socio socio = prestamo.getSocio();
                socio.setEstado(Socio.EstadoSocio.ACTIVO);
                socio.setFechaFinPenalizacion(null);
                socioRepository.save(socio);
            }
        }

        // Validación 2: Límite de préstamos
        long prestamosActivos = prestamoRepository.countBySocioAndFechaDevolucionIsNull(prestamo.getSocio());
        if (prestamosActivos >= 3) {
            throw new ReglaNegocioException("El socio ha alcanzado el límite de 3 préstamos activos.");
        }

        // Cálculo de fechas
        prestamo.setFechaPrestamo(LocalDateTime.now());
        prestamo.setFechaLimite(LocalDateTime.now().plusDays(2));

        return prestamoRepository.save(prestamo);
    }

    @Override
    public void devolverPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ReglaNegocioException("No se encontró el préstamo."));

        prestamo.setFechaDevolucion(LocalDateTime.now());

        Socio socio = prestamo.getSocio();
        if (prestamo.getFechaDevolucion().isAfter(prestamo.getFechaLimite())) {
            long diasDeRetraso = ChronoUnit.DAYS.between(prestamo.getFechaLimite(), prestamo.getFechaDevolucion());
            long diasDeSancion = diasDeRetraso * 2;
            socio.setEstado(Socio.EstadoSocio.SANCIONADO);
            socio.setFechaFinPenalizacion(LocalDate.now().plusDays(diasDeSancion));
        } else {
            socio.setEstado(Socio.EstadoSocio.ACTIVO);
            socio.setFechaFinPenalizacion(null);
        }

        socioRepository.save(socio);
        prestamoRepository.save(prestamo);
    }

    @Override
    public void borrarPrestamo(Long id) {
        prestamoRepository.deleteById(id);
    }
}
