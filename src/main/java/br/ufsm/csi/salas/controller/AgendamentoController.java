package br.ufsm.csi.salas.controller;

import br.ufsm.csi.salas.model.Agendamento;
import br.ufsm.csi.salas.model.Sala;
import br.ufsm.csi.salas.model.Funcionario;
import br.ufsm.csi.salas.service.AgendamentoService;
import br.ufsm.csi.salas.service.SalaService;
import br.ufsm.csi.salas.service.UsuarioService;
import br.ufsm.csi.salas.service.FuncionarioService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/agendamento")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final SalaService salaService;
    private final UsuarioService usuarioService;
    private final FuncionarioService funcionarioService;

    private static final int STATUS_ABERTO = 1;
    private static final int STATUS_EM_USO = 2;
    private static final int STATUS_FINALIZADO = 3;

    public AgendamentoController(AgendamentoService agendamentoService,
                                 SalaService salaService,
                                 UsuarioService usuarioService,
                                 FuncionarioService funcionarioService) {
        this.agendamentoService = agendamentoService;
        this.salaService = salaService;
        this.usuarioService = usuarioService;
        this.funcionarioService = funcionarioService;
    }

    @GetMapping("/novo")
    public String novoAgendamento(@RequestParam(name = "salaId", required = false) Integer salaId,
                                  Model model) {

        Agendamento agendamento = new Agendamento();
        agendamento.setStatus(STATUS_ABERTO); // Sempre começa como Aberto

        if (salaId != null) {
            Sala sala = salaService.buscar(salaId);
            if (sala != null) agendamento.setSala(sala);
        }

        model.addAttribute("agendamento", agendamento);
        carregarDadosFormulario(model);
        return "pages/agendar";
    }

    @PostMapping("/salvar")
    public String salvarAgendamento(@Valid @ModelAttribute("agendamento") Agendamento agendamento,
                                    BindingResult result,
                                    RedirectAttributes redirectAttributes,
                                    @RequestParam(defaultValue = "1") Integer repeatFor) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.agendamento", result);
            redirectAttributes.addFlashAttribute("agendamento", agendamento);
            carregarDadosFormularioComRedirect(redirectAttributes);
            return "redirect:/agendamento/novo";
        }

        try {
            Funcionario funcionarioPadrao = obterFuncionarioPadrao();
            if (funcionarioPadrao == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Nenhum funcionário cadastrado no sistema!");
                return "redirect:/agendamento/novo";
            }

            agendamento.setFuncionario(funcionarioPadrao);
            agendamento.setStatus(STATUS_ABERTO);

            List<Agendamento> agendamentos = criarAgendamentosRecorrentes(agendamento, repeatFor);
            int sucessos = 0;

            for (Agendamento ag : agendamentos) {
                String mensagem = agendamentoService.inserir(ag);
                if (mensagem.toLowerCase().contains("sucesso")) sucessos++;
            }

            String diaSemana = getDiaSemanaPortugues(agendamento.getData());
            redirectAttributes.addFlashAttribute("mensagemSucesso",
                    sucessos + " agendamentos criados para " + diaSemana + " por " + repeatFor + " semana(s)!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao salvar agendamento: " + e.getMessage());
            return "redirect:/agendamento/novo";
        }

        return "redirect:/agendamento";
    }

    @PostMapping("/editar")
    public String atualizarAgendamento(@Valid @ModelAttribute("agendamento") Agendamento agendamento,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.agendamento", result);
            redirectAttributes.addFlashAttribute("agendamento", agendamento);
            return "redirect:/agendamento/editar/" + agendamento.getId();
        }

        try {
            agendamentoService.alterar(agendamento);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Agendamento atualizado com sucesso!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar agendamento: " + e.getMessage());
            return "redirect:/agendamento/editar/" + agendamento.getId();
        }

        return "redirect:/agendamento";
    }

    @GetMapping("/disponibilidade")
    @ResponseBody
    public Map<String, Object> verificarDisponibilidadeJson(
            @RequestParam("salaId") Integer salaId,
            @RequestParam("turno") Integer turno,
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data) {

        Map<String, Object> resposta = new HashMap<>();

        try {
            boolean disponivel = agendamentoService.verificarDisponibilidade(salaId, turno, data);
            resposta.put("disponivel", disponivel);
            resposta.put("mensagem", disponivel
                    ? "Sala disponível para este turno"
                    : "Sala já ocupada neste turno");
        } catch (Exception e) {
            resposta.put("disponivel", false);
            resposta.put("mensagem", "Erro ao verificar disponibilidade: " + e.getMessage());
        }

        return resposta;
    }

    @GetMapping("/disponibilidade-edicao")
    @ResponseBody
    public Map<String, Object> verificarDisponibilidadeEdicao(
            @RequestParam("salaId") Integer salaId,
            @RequestParam("turno") Integer turno,
            @RequestParam("data") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam("agendamentoId") Integer agendamentoId) {

        Map<String, Object> resposta = new HashMap<>();

        try {
            boolean disponivel = agendamentoService.verificarDisponibilidadeEdicao(salaId, turno, data, agendamentoId);
            resposta.put("disponivel", disponivel);
            resposta.put("mensagem", disponivel
                    ? "Sala disponível para este turno (edição)"
                    : "Sala já ocupada neste turno");
        } catch (Exception e) {
            resposta.put("disponivel", false);
            resposta.put("mensagem", "Erro ao verificar disponibilidade: " + e.getMessage());
        }

        return resposta;
    }

    @GetMapping("/editar/{id}")
    public String editarAgendamento(@PathVariable("id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Agendamento agendamento = agendamentoService.buscarPorId(id);

            if (agendamento == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Agendamento não encontrado!");
                return "redirect:/agendamento";
            }

            model.addAttribute("agendamento", agendamento);
            carregarDadosFormulario(model); // Reutiliza método que popula salas e usuários

            return "pages/agendar"; // Usa o mesmo template do “novo agendamento”

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao carregar agendamento: " + e.getMessage());
            return "redirect:/agendamento";
        }
    }

    @GetMapping
    public String listarAgendamentos(@RequestParam(name = "status", required = false) Integer status,
                                     @RequestParam(name = "data", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                     @RequestParam(name = "bloco", required = false) String bloco,
                                     Model model) {
        try {
            List<Agendamento> agendamentos = (status != null)
                    ? agendamentoService.listarPorStatus(status)
                    : agendamentoService.listar();

            if (data != null)
                agendamentos = agendamentos.stream()
                        .filter(a -> a.getData() != null && a.getData().equals(data))
                        .collect(Collectors.toList());

            if (bloco != null && !bloco.isEmpty())
                agendamentos = agendamentos.stream()
                        .filter(a -> a.getSala() != null && bloco.equals(a.getSala().getBloco()))
                        .collect(Collectors.toList());

            if (agendamentos == null) agendamentos = new ArrayList<>();

            // Atualiza estatísticas dos cards
            long totalHoje = agendamentos.stream()
                    .filter(a -> a.getData() != null && a.getData().equals(LocalDate.now()))
                    .count();

            long totalAbertos = agendamentos.stream()
                    .filter(a -> a.getStatus() == STATUS_ABERTO)
                    .count();

            long totalEmUso = agendamentos.stream()
                    .filter(a -> a.getStatus() == STATUS_EM_USO)
                    .count();

            long totalFinalizados = agendamentos.stream()
                    .filter(a -> a.getStatus() == STATUS_FINALIZADO)
                    .count();

            model.addAttribute("agendamentos", agendamentos);
            model.addAttribute("totalHoje", totalHoje);
            model.addAttribute("totalAbertos", totalAbertos);
            model.addAttribute("totalEmUso", totalEmUso);
            model.addAttribute("totalFinalizados", totalFinalizados);
            model.addAttribute("blocos", Arrays.asList("A", "B", "C", "D", "E", "F", "G"));

        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao carregar agendamentos: " + e.getMessage());
            model.addAttribute("agendamentos", new ArrayList<>());
        }

        return "pages/agendamentos";
    }

    @GetMapping("/em-uso/{id}")
    public String colocarEmUso(@PathVariable("id") Integer id,
                               RedirectAttributes redirectAttributes) {
        try {
            Agendamento agendamento = agendamentoService.buscarPorId(id);

            if (agendamento == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Agendamento não encontrado!");
                return "redirect:/agendamento";
            }

            if (agendamento.getStatus() == STATUS_FINALIZADO) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Este agendamento já foi finalizado!");
                return "redirect:/agendamento";
            }

            if (agendamento.getStatus() == STATUS_EM_USO) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Este agendamento já está em uso!");
                return "redirect:/agendamento";
            }

            agendamento.setStatus(STATUS_EM_USO);
            agendamentoService.alterar(agendamento);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Agendamento colocado em uso com sucesso!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao colocar agendamento em uso: " + e.getMessage());
        }

        return "redirect:/agendamento";
    }

    @GetMapping("/finalizar/{id}")
    public String finalizarAgendamento(@PathVariable("id") Integer id,
                                       RedirectAttributes redirectAttributes) {
        try {
            Agendamento agendamento = agendamentoService.buscarPorId(id);

            if (agendamento == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Agendamento não encontrado!");
                return "redirect:/agendamento";
            }

            if (agendamento.getStatus() == STATUS_ABERTO) {
                redirectAttributes.addFlashAttribute("mensagemErro",
                        "Antes de finalizar, é necessário colocar o agendamento como 'Em Uso'.");
                return "redirect:/agendamento";
            }

            if (agendamento.getStatus() == STATUS_FINALIZADO) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Este agendamento já foi finalizado!");
                return "redirect:/agendamento";
            }

            agendamento.setStatus(STATUS_FINALIZADO);
            agendamentoService.alterar(agendamento);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Agendamento finalizado com sucesso!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao finalizar agendamento: " + e.getMessage());
        }

        return "redirect:/agendamento";
    }

    private void carregarDadosFormulario(Model model) {
        try {
            model.addAttribute("salas", salaService.listar());
            model.addAttribute("usuarios", usuarioService.listar());
        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao carregar dados do formulário: " + e.getMessage());
            model.addAttribute("salas", new ArrayList<>());
            model.addAttribute("usuarios", new ArrayList<>());
        }
    }

    private void carregarDadosFormularioComRedirect(RedirectAttributes redirectAttributes) {
        try {
            redirectAttributes.addFlashAttribute("salas", salaService.listar());
            redirectAttributes.addFlashAttribute("usuarios", usuarioService.listar());
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao carregar dados: " + e.getMessage());
        }
    }

    private Funcionario obterFuncionarioPadrao() {
        try {
            List<Funcionario> funcionarios = funcionarioService.listar();
            if (funcionarios != null && !funcionarios.isEmpty()) {
                return funcionarios.get(0);
            }
        } catch (Exception ignored) {}
        return null;
    }

    private List<Agendamento> criarAgendamentosRecorrentes(Agendamento agendamentoBase, int repeatFor) {
        List<Agendamento> agendamentos = new ArrayList<>();

        for (int i = 0; i < repeatFor; i++) {
            Agendamento agendamento = new Agendamento();
            agendamento.setSala(agendamentoBase.getSala());
            agendamento.setFuncionario(agendamentoBase.getFuncionario());
            agendamento.setUsuario(agendamentoBase.getUsuario());
            agendamento.setStatus(STATUS_ABERTO);
            agendamento.setTurno(agendamentoBase.getTurno());
            agendamento.setData(agendamentoBase.getData().plusWeeks(i));
            agendamento.setDatacadastro(LocalDate.now());
            agendamentos.add(agendamento);
        }

        return agendamentos;
    }

    @GetMapping("/excluir/{id}")
    public String excluirAgendamento(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            Agendamento agendamento = agendamentoService.buscarPorId(id);

            if (agendamento == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Agendamento não encontrado!");
                return "redirect:/agendamento";
            }

            if (agendamento.getStatus() == STATUS_FINALIZADO) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Não é possível excluir um agendamento finalizado!");
                return "redirect:/agendamento";
            }

            agendamentoService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", "Agendamento excluído com sucesso!");

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir agendamento: " + e.getMessage());
        }

        return "redirect:/agendamento";
    }

    private String getDiaSemanaPortugues(LocalDate data) {
        String[] dias = {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
        return dias[data.getDayOfWeek().getValue() % 7];
    }
}
