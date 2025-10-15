package br.ufsm.csi.salas.controller;

import br.ufsm.csi.salas.service.SalaService;
import br.ufsm.csi.salas.model.Sala;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/sala")
public class SalaController {

    @GetMapping
    public String listarsalas(Model model) {
        model.addAttribute("salas", new SalaService().listar());
        if(!model.containsAttribute("sala")) {
            model.addAttribute("sala", new Sala());
        }
        return "pages/salas";
    }



}
