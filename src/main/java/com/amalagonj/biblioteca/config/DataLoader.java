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
// Esta clase esta generada con IA para añadir Datos de prueba
    @Override
    public void run(String... args) throws Exception {
        // 1. Crear o recuperar Socios
        Socio juan = createSocioIfNotFound("Juan", "Pérez", "juan.perez@example.com", LocalDate.of(1990, 1, 1));
        Socio ana = createSocioIfNotFound("Ana", "García", "ana.garcia@example.com", LocalDate.of(1985, 5, 10));
        Socio carlos = createSocioIfNotFound("Carlos", "López", "carlos.lopez@example.com", LocalDate.of(1992, 8, 20));
        Socio maria = createSocioIfNotFound("Maria", "Rodriguez", "maria.rodriguez@example.com", LocalDate.of(1988, 3, 15));

        // 2. Crear o recuperar Libros
        Libro lotr = createLibroIfNotFound("El Señor de los Anillos", "J.R.R. Tolkien", "Fantasía", "978-84-450-7032-8");
        Libro cienAnos = createLibroIfNotFound("Cien años de soledad", "Gabriel García Márquez", "Realismo mágico", "978-84-376-0494-7");
        Libro quijote = createLibroIfNotFound("Don Quijote de la Mancha", "Miguel de Cervantes", "Clásico", "978-84-204-1214-6");
        Libro orwell = createLibroIfNotFound("1984", "George Orwell", "Ciencia Ficción", "978-84-08-00386-6");
        Libro principito = createLibroIfNotFound("El Principito", "Antoine de Saint-Exupéry", "Infantil", "978-84-9838-149-8");

        // 3. Crear Préstamos de ejemplo (solo si la tabla está vacía)
        if (prestamoRepository.count() == 0) {
            
            // Caso A: Préstamo activo y en plazo (Juan tiene LOTR)
            // Prestado hoy, vence en 2 días.
            createPrestamo(juan, lotr, LocalDateTime.now(), null);

            // Caso B: Préstamo devuelto a tiempo (Ana tuvo Cien Años)
            // Prestado hace 10 días, vencía hace 8 días, devuelto hace 8 días.
            createPrestamo(ana, cienAnos, LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(8));

            // Caso C: Préstamo devuelto con retraso -> Sanción activa (Carlos tuvo Quijote)
            // Prestado hace 5 días, vencía hace 3 días. Devuelto HOY (3 días tarde).
            // Sanción: 3 días retraso * 2 = 6 días de penalización a partir de hoy.
            createPrestamo(carlos, quijote, LocalDateTime.now().minusDays(5), LocalDateTime.now());
            
            // Actualizamos estado de Carlos para reflejar la sanción
            carlos.setEstado(Socio.EstadoSocio.SANCIONADO);
            carlos.setFechaFinPenalizacion(LocalDate.now().plusDays(6));
            socioRepository.save(carlos);

            // Caso D: Préstamo activo pero vencido (Maria tiene 1984)
            // Prestado hace 4 días, vencía hace 2 días. No devuelto aún.
            createPrestamo(maria, orwell, LocalDateTime.now().minusDays(4), null);
            
            // Caso E: Otro préstamo activo en plazo (Juan tiene El Principito)
            createPrestamo(juan, principito, LocalDateTime.now().minusHours(2), null);
        }
    }

    private Socio createSocioIfNotFound(String nombre, String apellidos, String email, LocalDate fechaNacimiento) {
        return socioRepository.findByEmail(email).orElseGet(() -> {
            Socio socio = new Socio();
            socio.setNombre(nombre);
            socio.setApellidos(apellidos);
            socio.setEmail(email);
            socio.setFechaNacimiento(fechaNacimiento);
            socio.setFechaAlta(LocalDate.now());
            socio.setEstado(Socio.EstadoSocio.ACTIVO);
            return socioRepository.save(socio);
        });
    }

    private Libro createLibroIfNotFound(String titulo, String autor, String categoria, String isbn) {
        return libroRepository.findByIsbn(isbn).orElseGet(() -> {
            Libro libro = new Libro();
            libro.setTitulo(titulo);
            libro.setAutor(autor);
            libro.setCategoria(categoria);
            libro.setIsbn(isbn);
            return libroRepository.save(libro);
        });
    }

    private void createPrestamo(Socio socio, Libro libro, LocalDateTime fechaPrestamo, LocalDateTime fechaDevolucion) {
        Prestamo prestamo = new Prestamo();
        prestamo.setSocio(socio);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(fechaPrestamo);
        // Regla estricta: Fecha límite = Fecha Préstamo + 2 días
        prestamo.setFechaLimite(fechaPrestamo.plusDays(2));
        prestamo.setFechaDevolucion(fechaDevolucion);
        prestamoRepository.save(prestamo);
    }
}
