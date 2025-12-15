package com.amalagonj.biblioteca.config;

import com.amalagonj.biblioteca.entidades.Libro;
import com.amalagonj.biblioteca.entidades.Prestamo;
import com.amalagonj.biblioteca.entidades.Socio;
import com.amalagonj.biblioteca.repositories.LibroRepository;
import com.amalagonj.biblioteca.repositories.PrestamoRepository;
import com.amalagonj.biblioteca.repositories.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private LibroRepository libroRepository;

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Override
    public void run(String... args) throws Exception {
        // Socios
        socioRepository.findByEmail("juan.perez@example.com").orElseGet(() -> {
            Socio socio1 = new Socio();
            socio1.setNombre("Juan");
            socio1.setApellidos("Pérez");
            socio1.setEmail("juan.perez@example.com");
            socio1.setFechaNacimiento(LocalDate.of(1990, 1, 1));
            socio1.setFechaAlta(LocalDate.now());
            socio1.setEstado(Socio.EstadoSocio.ACTIVO);
            return socioRepository.save(socio1);
        });

        socioRepository.findByEmail("ana.garcia@example.com").orElseGet(() -> {
            Socio socio2 = new Socio();
            socio2.setNombre("Ana");
            socio2.setApellidos("García");
            socio2.setEmail("ana.garcia@example.com");
            socio2.setFechaNacimiento(LocalDate.of(1985, 5, 10));
            socio2.setFechaAlta(LocalDate.now());
            socio2.setEstado(Socio.EstadoSocio.ACTIVO);
            return socioRepository.save(socio2);
        });

        // Libros
        libroRepository.findByIsbn("978-84-450-7032-8").orElseGet(() -> {
            Libro libro1 = new Libro();
            libro1.setTitulo("El Señor de los Anillos");
            libro1.setAutor("J.R.R. Tolkien");
            libro1.setCategoria("Fantasía");
            libro1.setIsbn("978-84-450-7032-8");
            return libroRepository.save(libro1);
        });

        libroRepository.findByIsbn("978-84-376-0494-7").orElseGet(() -> {
            Libro libro2 = new Libro();
            libro2.setTitulo("Cien años de soledad");
            libro2.setAutor("Gabriel García Márquez");
            libro2.setCategoria("Realismo mágico");
            libro2.setIsbn("978-84-376-0494-7");
            return libroRepository.save(libro2);
        });

        // Prestamos
        if (prestamoRepository.count() == 0) {
            Socio socio1 = socioRepository.findByEmail("juan.perez@example.com").get();
            Libro libro1 = libroRepository.findByIsbn("978-84-450-7032-8").get();
            Prestamo prestamo1 = new Prestamo();
            prestamo1.setSocio(socio1);
            prestamo1.setLibro(libro1);
            prestamo1.setFechaPrestamo(LocalDateTime.now().minusDays(5));
            prestamo1.setFechaLimite(LocalDateTime.now().plusDays(10));
            prestamoRepository.save(prestamo1);

            Socio socio2 = socioRepository.findByEmail("ana.garcia@example.com").get();
            Libro libro2 = libroRepository.findByIsbn("978-84-376-0494-7").get();
            Prestamo prestamo2 = new Prestamo();
            prestamo2.setSocio(socio2);
            prestamo2.setLibro(libro2);
            prestamo2.setFechaPrestamo(LocalDateTime.now().minusDays(2));
            prestamo2.setFechaLimite(LocalDateTime.now().plusDays(13));
            prestamoRepository.save(prestamo2);
        }
    }
}
