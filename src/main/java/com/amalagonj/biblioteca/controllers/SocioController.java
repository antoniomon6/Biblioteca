package com.amalagonj.biblioteca.controllers;

import com.amalagonj.biblioteca.entidades.Socio;
import com.amalagonj.biblioteca.services.SocioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/socios")
public class SocioController {

    @Autowired
    private SocioService socioService;

    @GetMapping
    public String listarSocios(@RequestParam(value = "keyword", required = false) String keyword, Model model) {
        List<Socio> socios;
        if (keyword != null && !keyword.isEmpty()) {
            socios = socioService.findByNombreContaining(keyword);
        } else {
            socios = socioService.findAll();
        }
        model.addAttribute("socios", socios);
        return "socios";
    }

    @GetMapping("/nuevo")
    public String formularioNuevoSocio(Model model) {
        model.addAttribute("socio", new Socio());
        return "form-socio";
    }

    @GetMapping("/editar/{id}")
    public String formularioEditarSocio(@PathVariable Long id, Model model) {
        socioService.findById(id).ifPresent(socio -> model.addAttribute("socio", socio));
        return "form-socio";
    }

    @PostMapping("/guardar")
    public String guardarSocio(@ModelAttribute Socio socio) {
        socioService.save(socio);
        return "redirect:/socios";
    }

    @PostMapping("/eliminar")
    public String eliminarSocio(@RequestParam("id") Long id) {
        socioService.deleteById(id);
        return "redirect:/socios";
    }
}
