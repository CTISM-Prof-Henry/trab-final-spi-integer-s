package br.ufsm.csi.salas.controller;

import br.ufsm.csi.salas.model.Sala;
import br.ufsm.csi.salas.service.SalaService;
import br.ufsm.csi.salas.service.DashboardService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/sala")
public class SalaController {

    private final SalaService salaService;
    private final DashboardService dashboardService;

    // Lista de blocos disponíveis
    private final List<String> blocos = Arrays.asList("A", "B", "C", "D", "E", "F", "G");

    // Lista de tipos de sala disponíveis
    private final List<String> tiposSala = Arrays.asList(
            "Sala de Aula", "Laboratório", "Auditório", "Sala de Reunião",
            "Sala de Estudo", "Oficina", "Outros"
    );

    public SalaController(SalaService salaService, DashboardService dashboardService) {
        this.salaService = salaService;
        this.dashboardService = dashboardService;
    }

    // Página principal - LISTAR SALAS
    @GetMapping
    public String listarSalas(Model model) {
        // Dados das salas
        List<Sala> salas = salaService.listar();
        model.addAttribute("salas", salas);

        // Dados dinâmicos para os cards
        int salasEmUso = dashboardService.getSalasEmUso();
        int salasLivres = dashboardService.getSalasLivres();
        String turnoAtual = dashboardService.getNomeTurnoAtual();

        model.addAttribute("salasEmUso", salasEmUso);
        model.addAttribute("salasLivres", salasLivres);
        model.addAttribute("turnoAtual", turnoAtual);
        model.addAttribute("salasEmUsoIds", dashboardService.getSalasEmUsoIds());

        // Dados para filtros
        model.addAttribute("blocos", blocos);

        // Extrair tipos únicos de sala
        List<String> tiposUnicos = salas.stream()
                .map(Sala::getTipo)
                .distinct()
                .collect(Collectors.toList());
        model.addAttribute("tiposSala", tiposUnicos);

        return "pages/salas";
    }

    // Página de CADASTRO - GET
    @GetMapping("/cadastro")
    public String mostrarFormularioCadastro(Model model) {
        if (!model.containsAttribute("sala")) {
            model.addAttribute("sala", new Sala());
        }
        model.addAttribute("blocos", blocos);
        model.addAttribute("tiposSala", tiposSala);
        return "pages/cadastro-sala";
    }

    // Processar CADASTRO - POST
    @PostMapping("/cadastrar")
    public String cadastrarSala(@Valid @ModelAttribute("sala") Sala sala,
                                BindingResult result,
                                RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.sala", result);
            redirectAttributes.addFlashAttribute("sala", sala);
            return "redirect:/sala/cadastro";
        }

        String mensagem = salaService.inserir(sala);
        redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);

        return "redirect:/sala";
    }

    // Página de EDIÇÃO - GET
    @GetMapping("/editar/{id}")
    public String mostrarFormularioEdicao(@PathVariable("id") Integer id, Model model) {
        Sala sala = salaService.buscar(id);
        if (sala == null || sala.getId() == null) {
            model.addAttribute("mensagemErro", "Sala não encontrada!");
            return "redirect:/sala";
        }

        model.addAttribute("sala", sala);
        model.addAttribute("blocos", blocos);
        model.addAttribute("tiposSala", tiposSala);
        return "pages/cadastro-sala";
    }

    // Processar EDIÇÃO - POST
    @PostMapping("/editar")
    public String editarSala(@Valid @ModelAttribute("sala") Sala sala,
                             BindingResult result,
                             RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.sala", result);
            redirectAttributes.addFlashAttribute("sala", sala);
            return "redirect:/sala/editar/" + sala.getId();
        }

        String mensagem = salaService.alterar(sala);
        redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);

        return "redirect:/sala";
    }

    // EXCLUIR sala
    @GetMapping("/excluir/{id}")
    public String excluirSala(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        String mensagem = salaService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);
        return "redirect:/sala";
    }
}