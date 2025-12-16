package com.amalagonj.biblioteca.controllers;

import com.amalagonj.biblioteca.entidades.Libro;
import com.amalagonj.biblioteca.entidades.Prestamo;
import com.amalagonj.biblioteca.entidades.Socio;
import com.amalagonj.biblioteca.excepciones.ReglaNegocioException;
import com.amalagonj.biblioteca.repositories.LibroRepository;
import com.amalagonj.biblioteca.repositories.SocioRepository;
import com.amalagonj.biblioteca.services.PrestamoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;
    @Autowired
    private SocioRepository socioRepository;
    @Autowired
    private LibroRepository libroRepository;
    @GetMapping("/prestamos")
    public String listarPrestamos(Model model) {
        List<Prestamo> prestamos = prestamoService.listarTodosLosPrestamos();
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
    public String guardarPrestamo(Prestamo prestamo, RedirectAttributes redirectAttributes) {
        try {
            prestamoService.guardarPrestamo(prestamo);
            return "redirect:/prestamos";
        } catch (ReglaNegocioException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/prestamos/nuevo";
        }
    }
    @PostMapping("/prestamos/borrar")
    public String borrarPrestamo(@RequestParam("id") Long id) {
        prestamoService.borrarPrestamo(id);
        return "redirect:/prestamos";
    }
    @PostMapping("/prestamos/devolver")
    public String devolverPrestamo(@RequestParam("id") Long id) {
        prestamoService.devolverPrestamo(id);
        return "redirect:/prestamos";
    }
}
