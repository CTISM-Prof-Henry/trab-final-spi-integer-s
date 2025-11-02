package br.ufsm.csi.salas.controller;

import br.ufsm.csi.salas.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
public class HomeController {

    private final DashboardService dashboardService;

    public HomeController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public String home(Model model) {
        // Dados para os cards do dashboard
        model.addAttribute("salasEmUso", dashboardService.getSalasEmUso());
        model.addAttribute("salasLivres", dashboardService.getSalasLivres());
        model.addAttribute("agendamentosHoje", dashboardService.getAgendamentosHoje());
        model.addAttribute("turnoAtual", dashboardService.getNomeTurnoAtual());

        return "pages/home";
    }
}