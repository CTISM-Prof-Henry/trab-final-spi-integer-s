package br.ufsm.csi.salas.controller;

import br.ufsm.csi.salas.model.Agendamento;
import br.ufsm.csi.salas.model.Sala;
import br.ufsm.csi.salas.service.AgendamentoService;
import br.ufsm.csi.salas.service.SalaService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/agendamento")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final SalaService salaService;

    public AgendamentoController(AgendamentoService agendamentoService,
                                 SalaService salaService) {
        this.agendamentoService = agendamentoService;
        this.salaService = salaService;
    }

    // Novo agendamento - GET
    @GetMapping("/novo")
    public String novoAgendamento(Model model) {
        if (!model.containsAttribute("agendamento")) {
            Agendamento agendamento = new Agendamento();
            // Definir status padrão como Pendente (3)
            agendamento.setStatus(3);
            model.addAttribute("agendamento", agendamento);
        }

        // Carregar dados para os selects
        carregarDadosFormulario(model);

        return "pages/agendar"; // ← CORREÇÃO: "pages/agendar"
    }

    // Salvar agendamento - POST
    @PostMapping("/salvar")
    public String salvarAgendamento(@Valid @ModelAttribute("agendamento") Agendamento agendamento,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.agendamento", result);
            redirectAttributes.addFlashAttribute("agendamento", agendamento);
            return "redirect:/agendamento/novo";
        }

        String mensagem = agendamentoService.inserir(agendamento);
        redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);

        return "redirect:/agendamento";
    }

    // Editar agendamento - GET
    @GetMapping("/editar/{id}")
    public String editarAgendamento(@PathVariable("id") Integer id, Model model) {
        Agendamento agendamento = agendamentoService.buscarPorId(id);
        if (agendamento == null) {
            model.addAttribute("mensagemErro", "Agendamento não encontrado!");
            return "redirect:/agendamento";
        }

        model.addAttribute("agendamento", agendamento);
        carregarDadosFormulario(model);

        return "pages/agendar"; // ← CORREÇÃO: "pages/agendar"
    }

    // Atualizar agendamento - POST
    @PostMapping("/editar")
    public String atualizarAgendamento(@Valid @ModelAttribute("agendamento") Agendamento agendamento,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.agendamento", result);
            redirectAttributes.addFlashAttribute("agendamento", agendamento);
            return "redirect:/agendamento/editar/" + agendamento.getId();
        }

        String mensagem = agendamentoService.alterar(agendamento);
        redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);

        return "redirect:/agendamento";
    }

    // Excluir agendamento
    @GetMapping("/excluir/{id}")
    public String excluirAgendamento(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        String mensagem = agendamentoService.excluir(id);
        redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);
        return "redirect:/agendamento";
    }

    // Listar agendamentos (método principal)
    @GetMapping
    public String listarAgendamentos(@RequestParam(name = "status", required = false) Integer status,
                                     Model model) {
        List<Agendamento> agendamentos;

        if (status != null) {
            agendamentos = agendamentoService.listarPorStatus(status);
        } else {
            agendamentos = agendamentoService.listar();
        }

        // Garantir que a lista não seja nula
        if (agendamentos == null) {
            agendamentos = new ArrayList<>();
        }

        model.addAttribute("agendamentos", agendamentos);
        model.addAttribute("agendamentosConcluidos", 0);
        model.addAttribute("agendamentosAndamento", 0);
        model.addAttribute("agendamentosHoje", agendamentos.size());
        model.addAttribute("blocos", Arrays.asList("A", "B", "C", "D", "E", "F", "G"));

        return "pages/agendamentos";
    }

    // Método auxiliar para carregar dados do formulário
    private void carregarDadosFormulario(Model model) {
        List<Sala> salas = salaService.listar();
        model.addAttribute("salas", salas);
    }
}