package com.amalagonj.biblioteca.controllers;

import com.amalagonj.biblioteca.entidades.Libro;
import com.amalagonj.biblioteca.entidades.Prestamo;
import com.amalagonj.biblioteca.entidades.Socio;
import com.amalagonj.biblioteca.repositories.LibroRepository;
import com.amalagonj.biblioteca.repositories.PrestamoRepository;
import com.amalagonj.biblioteca.repositories.SocioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PrestamoController {

    @Autowired
    private PrestamoRepository prestamoRepository;

    @Autowired
    private SocioRepository socioRepository;

    @Autowired
    private LibroRepository libroRepository;

    @GetMapping("/prestamos")
    public String listarPrestamos(Model model) {
        List<Prestamo> prestamos = prestamoRepository.findAll();
        model.addAttribute("prestamos", prestamos);
        return "prestamos";
    }

    @GetMapping("/prestamos/nuevo")
    public String formularioNuevoPrestamo(Model model) {
        List<Socio> socios = socioRepository.findAll();
        List<Libro> libros = libroRepository.findAll();
        model.addAttribute("prestamo", new Prestamo());
        model.addAttribute("socios", socios);
        model.addAttribute("libros", libros);
        return "prestamo_form";
    }

    @PostMapping("/prestamos")
    public String guardarPrestamo(Prestamo prestamo) {
        prestamo.setFechaPrestamo(LocalDateTime.now());
        prestamo.setFechaLimite(LocalDateTime.now().plusDays(15));
        prestamoRepository.save(prestamo);
        return "redirect:/prestamos";
    }

    @PostMapping("/prestamos/borrar")
    public String borrarPrestamo(@RequestParam("id") Long id) {
        prestamoRepository.deleteById(id);
        return "redirect:/prestamos";
    }
}
