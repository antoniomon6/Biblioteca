package com.amalagonj.biblioteca.controllers;

import com.amalagonj.biblioteca.entidades.Libro;
import com.amalagonj.biblioteca.services.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    public String listarLibros(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Libro> libros;
        if (keyword != null && !keyword.isEmpty()) {
            libros = libroService.findByTituloContaining(keyword);
        } else {
            libros = libroService.findAll();
        }
        model.addAttribute("libros", libros);
        return "libros";
    }

    @GetMapping("/nuevo")
    public String formularioNuevoLibro(Model model) {
        model.addAttribute("libro", new Libro());
        return "form-libro";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditarLibro(@PathVariable Long id, Model model) {
        libroService.findById(id).ifPresent(libro -> model.addAttribute("libro", libro));
        return "form-libro";
    }

    @PostMapping("/guardar")
    public String guardarLibro(@ModelAttribute Libro libro) {
        libroService.save(libro);
        return "redirect:/libros";
    }

    @PostMapping("/eliminar")
    public String eliminarLibro(@RequestParam("id") Long id) {
        libroService.deleteById(id);
        return "redirect:/libros";
    }
}
