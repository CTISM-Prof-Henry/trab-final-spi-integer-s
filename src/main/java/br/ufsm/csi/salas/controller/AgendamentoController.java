package br.ufsm.csi.salas.controller;

import br.ufsm.csi.salas.model.Agendamento;
import br.ufsm.csi.salas.model.Sala;
import br.ufsm.csi.salas.model.Usuario;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/agendamento")
public class AgendamentoController {

    private final AgendamentoService agendamentoService;
    private final SalaService salaService;
    private final UsuarioService usuarioService;
    private final FuncionarioService funcionarioService;

    public AgendamentoController(AgendamentoService agendamentoService,
                                 SalaService salaService,
                                 UsuarioService usuarioService,
                                 FuncionarioService funcionarioService) {
        this.agendamentoService = agendamentoService;
        this.salaService = salaService;
        this.usuarioService = usuarioService;
        this.funcionarioService = funcionarioService;
    }

    // =====================================
    // NOVO AGENDAMENTO - GET
    // =====================================
    @GetMapping("/novo")
    public String novoAgendamento(Model model) {
        if (!model.containsAttribute("agendamento")) {
            Agendamento agendamento = new Agendamento();
            agendamento.setStatus(3); // status padrão: Pendente
            model.addAttribute("agendamento", agendamento);
        }

        // Garante que os selects sempre tenham dados
        if (!model.containsAttribute("salas") || !model.containsAttribute("usuarios")) {
            carregarDadosFormulario(model);
        }

        return "pages/agendar";
    }

    // =====================================
    // EDITAR - GET
    // =====================================
    @GetMapping("/editar/{id}")
    public String editarAgendamento(@PathVariable("id") Integer id,
                                    Model model,
                                    RedirectAttributes redirectAttributes) {

        System.out.println("=== TENTANDO EDITAR AGENDAMENTO ID: " + id + " ===");

        try {
            Agendamento agendamento = agendamentoService.buscarPorId(id);
            System.out.println("Agendamento encontrado: " + (agendamento != null));

            if (agendamento == null) {
                redirectAttributes.addFlashAttribute("mensagemErro", "Agendamento não encontrado!");
                return "redirect:/agendamento";
            }

            // ✅ CORREÇÃO: Adicionar flash attributes para garantir que os dados cheguem na view
            model.addAttribute("agendamento", agendamento);

            // ✅ CORREÇÃO: Carregar dados SEMPRE, mesmo se já existirem no model
            carregarDadosFormulario(model);

            // ✅ DEBUG: Verificar dados carregados
            System.out.println("Sala do agendamento: " + (agendamento.getSala() != null ? agendamento.getSala().getId() : "null"));
            System.out.println("Usuário do agendamento: " + (agendamento.getUsuario() != null ? agendamento.getUsuario().getId() : "null"));

        } catch (Exception e) {
            System.out.println("ERRO ao carregar agendamento: " + e.getMessage());
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao carregar agendamento: " + e.getMessage());
            return "redirect:/agendamento";
        }

        return "pages/agendar";
    }

    // =====================================
    // EDITAR - POST
    // =====================================
    @PostMapping("/editar")
    public String atualizarAgendamento(@Valid @ModelAttribute("agendamento") Agendamento agendamento,
                                       BindingResult result,
                                       RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.agendamento", result);
            redirectAttributes.addFlashAttribute("agendamento", agendamento);
            carregarDadosFormularioComRedirect(redirectAttributes);
            return "redirect:/agendamento/editar/" + agendamento.getId();
        }

        try {
            // ✅ CORREÇÃO: Verificar se os objetos relacionados têm IDs
            if (agendamento.getSala() != null && agendamento.getSala().getId() == null) {
                result.rejectValue("sala", "error.agendamento", "Sala inválida");
            }

            if (agendamento.getUsuario() != null && agendamento.getUsuario().getId() == null) {
                result.rejectValue("usuario", "error.agendamento", "Usuário inválido");
            }

            if (result.hasErrors()) {
                redirectAttributes.addFlashAttribute("org.springframework.validation.BindingResult.agendamento", result);
                redirectAttributes.addFlashAttribute("agendamento", agendamento);
                carregarDadosFormularioComRedirect(redirectAttributes);
                return "redirect:/agendamento/editar/" + agendamento.getId();
            }

            // ✅ CORREÇÃO: Garantir que o funcionário está associado
            if (agendamento.getFuncionario() == null || agendamento.getFuncionario().getId() == null) {
                Funcionario funcionarioPadrao = obterFuncionarioPadrao();
                if (funcionarioPadrao != null) {
                    agendamento.setFuncionario(funcionarioPadrao);
                }
            }

            String mensagem = agendamentoService.alterar(agendamento);
            redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao atualizar agendamento: " + e.getMessage());
            // ✅ CORREÇÃO: Recarregar dados ao retornar para edição
            carregarDadosFormularioComRedirect(redirectAttributes);
            return "redirect:/agendamento/editar/" + agendamento.getId();
        }

        return "redirect:/agendamento";
    }

    // =====================================
    // EXCLUIR
    // =====================================
    @GetMapping("/excluir/{id}")
    public String excluirAgendamento(@PathVariable("id") Integer id, RedirectAttributes redirectAttributes) {
        try {
            String mensagem = agendamentoService.excluir(id);
            redirectAttributes.addFlashAttribute("mensagemSucesso", mensagem);
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("mensagemErro", "Erro ao excluir agendamento: " + e.getMessage());
        }
        return "redirect:/agendamento";
    }

    // =====================================
    // LISTAR
    // =====================================
    @GetMapping
    public String listarAgendamentos(@RequestParam(name = "status", required = false) Integer status,
                                     @RequestParam(name = "data", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                     @RequestParam(name = "bloco", required = false) String bloco,
                                     Model model) {
        try {
            List<Agendamento> agendamentos;

            if (status != null) {
                agendamentos = agendamentoService.listarPorStatus(status);
            } else {
                agendamentos = agendamentoService.listar();
            }

            // DEBUG: Verificar os IDs
            System.out.println("=== DEBUG: Listando agendamentos ===");
            for (Agendamento ag : agendamentos) {
                System.out.println("Agendamento ID: " + ag.getId() +
                        ", Sala: " + (ag.getSala() != null ? ag.getSala().getId() : "null") +
                        ", Data: " + ag.getData());
            }

            // Aplicar filtro por data se fornecido
            if (data != null) {
                agendamentos = agendamentos.stream()
                        .filter(a -> a.getData() != null && a.getData().equals(data))
                        .collect(Collectors.toList());
            }

            // Aplicar filtro por bloco se fornecido
            if (bloco != null && !bloco.isEmpty()) {
                agendamentos = agendamentos.stream()
                        .filter(a -> a.getSala() != null && bloco.equals(a.getSala().getBloco()))
                        .collect(Collectors.toList());
            }

            if (agendamentos == null) {
                agendamentos = new ArrayList<>();
            }

            // Calcular estatísticas para os cards
            long agendamentosHoje = agendamentos.stream()
                    .filter(a -> a.getData() != null && a.getData().equals(LocalDate.now()))
                    .count();

            long agendamentosPendentes = agendamentos.stream()
                    .filter(a -> a.getStatus() == 3)
                    .count();

            long agendamentosAprovados = agendamentos.stream()
                    .filter(a -> a.getStatus() == 1)
                    .count();

            List<Funcionario> funcionarios = funcionarioService.listar();
            if (funcionarios != null && !funcionarios.isEmpty()) {
                model.addAttribute("usuarioLogado", funcionarios.get(0));
            }

            model.addAttribute("agendamentos", agendamentos);
            model.addAttribute("agendamentosPendentes", agendamentosPendentes);
            model.addAttribute("agendamentosAprovados", agendamentosAprovados);
            model.addAttribute("agendamentosHoje", agendamentosHoje);
            model.addAttribute("blocos", Arrays.asList("A", "B", "C", "D", "E", "F", "G"));

        } catch (Exception e) {
            model.addAttribute("mensagemErro", "Erro ao carregar agendamentos: " + e.getMessage());
            model.addAttribute("agendamentos", new ArrayList<>());
            model.addAttribute("agendamentosPendentes", 0);
            model.addAttribute("agendamentosAprovados", 0);
            model.addAttribute("agendamentosHoje", 0);
        }

        return "pages/agendamentos";
    }

    // =====================================
    // MÉTODOS AUXILIARES
    // =====================================
    private void carregarDadosFormulario(Model model) {
        try {
            List<Sala> salas = salaService.listar();
            List<Usuario> usuarios = usuarioService.listar();

            // ✅ CORREÇÃO: Sempre adicionar os atributos, mesmo se já existirem
            model.addAttribute("salas", salas != null ? salas : new ArrayList<>());
            model.addAttribute("usuarios", usuarios != null ? usuarios : new ArrayList<>());

            // ✅ DEBUG
            System.out.println("Salas carregadas: " + (salas != null ? salas.size() : 0));
            System.out.println("Usuários carregados: " + (usuarios != null ? usuarios.size() : 0));

        } catch (Exception e) {
            System.out.println("ERRO ao carregar dados do formulário: " + e.getMessage());
            model.addAttribute("mensagemErro", "Erro ao carregar dados do formulário: " + e.getMessage());
            model.addAttribute("salas", new ArrayList<>());
            model.addAttribute("usuarios", new ArrayList<>());
        }
    }

    private void carregarDadosFormularioComRedirect(RedirectAttributes redirectAttributes) {
        try {
            List<Sala> salas = salaService.listar();
            List<Usuario> usuarios = usuarioService.listar();

            // ✅ CORREÇÃO: Usar flash attributes corretamente
            redirectAttributes.addFlashAttribute("salas", salas != null ? salas : new ArrayList<>());
            redirectAttributes.addFlashAttribute("usuarios", usuarios != null ? usuarios : new ArrayList<>());

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

    @ModelAttribute("usuarioLogado")
    public Funcionario getUsuarioLogado() {
        try {
            List<Funcionario> funcionarios = funcionarioService.listar();
            if (funcionarios != null && !funcionarios.isEmpty()) {
                return funcionarios.get(0);
            }
        } catch (Exception ignored) {}

        Funcionario funcionarioPadrao = new Funcionario();
        funcionarioPadrao.setId(1);
        funcionarioPadrao.setNome("Administrador");
        funcionarioPadrao.setSenha("1");
        funcionarioPadrao.setCpf("123456789");
        funcionarioPadrao.setPermissao(1);
        funcionarioPadrao.setEmail("e@g");
        return funcionarioPadrao;
    }

    @GetMapping("/disponibilidade")
    @ResponseBody
    public boolean verificarDisponibilidade(@RequestParam Integer salaId,
                                            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
                                            @RequestParam Integer turno) {

        System.out.println("=== VERIFICANDO DISPONIBILIDADE NORMAL ===");
        System.out.println("Sala ID: " + salaId + ", Data: " + data + ", Turno: " + turno);

        List<Agendamento> agendamentos = agendamentoService.buscarPorData(data);
        System.out.println("Agendamentos encontrados: " + (agendamentos != null ? agendamentos.size() : 0));

        boolean disponivel = agendamentos.stream()
                .noneMatch(a -> a != null &&
                        a.getSala() != null &&
                        a.getSala().getId() != null &&
                        a.getSala().getId().equals(salaId) &&
                        a.getTurno() == turno &&
                        a.getStatus() != 2);

        System.out.println("Disponível: " + disponivel);
        return disponivel;
    }

    // =====================================
    // SALVAR COM REPETIÇÃO - POST
    // =====================================
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
            agendamento.setStatus(3); // Pendente

            // Criar múltiplos agendamentos para o mesmo dia da semana
            List<Agendamento> agendamentos = criarAgendamentosRecorrentes(agendamento, repeatFor);
            int sucessos = 0;

            for (Agendamento agend : agendamentos) {
                String mensagem = agendamentoService.inserir(agend);
                if (mensagem.contains("sucesso") || mensagem.contains("Sucesso")) {
                    sucessos++;
                }
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

    // =====================================
    // MÉTODO PARA CRIAR AGENDAMENTOS RECORRENTES
    // =====================================
    private List<Agendamento> criarAgendamentosRecorrentes(Agendamento agendamentoBase, int repeatFor) {
        List<Agendamento> agendamentos = new ArrayList<>();

        for (int i = 0; i < repeatFor; i++) {
            Agendamento agendamento = new Agendamento();
            agendamento.setSala(agendamentoBase.getSala());
            agendamento.setFuncionario(agendamentoBase.getFuncionario());
            agendamento.setUsuario(agendamentoBase.getUsuario());
            agendamento.setStatus(agendamentoBase.getStatus());
            agendamento.setTurno(agendamentoBase.getTurno());

            // Calcular data: mesma dia da semana, semanas seguintes
            LocalDate dataBase = agendamentoBase.getData();
            LocalDate novaData = dataBase.plusWeeks(i);

            agendamento.setData(novaData);
            agendamento.setDatacadastro(LocalDate.now());

            agendamentos.add(agendamento);
        }

        return agendamentos;
    }

    // =====================================
    // VERIFICAR DISPONIBILIDADE RECORRENTE
    // =====================================
    @GetMapping("/disponibilidade-recorrente")
    @ResponseBody
    public Map<String, Object> verificarDisponibilidadeRecorrente(
            @RequestParam Integer salaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam Integer turno,
            @RequestParam Integer repeatFor) {

        Map<String, Object> resultado = new HashMap<>();
        List<String> conflitos = new ArrayList<>();

        System.out.println("=== VERIFICANDO DISPONIBILIDADE RECORRENTE ===");
        System.out.println("Sala ID: " + salaId + ", Data: " + data + ", Turno: " + turno + ", Repeat: " + repeatFor);

        // Para cada semana
        for (int i = 0; i < repeatFor; i++) {
            LocalDate dataVerificacao = data.plusWeeks(i);

            // Verificar se já existe agendamento para esta data
            boolean disponivel = verificarDisponibilidadeUnica(salaId, dataVerificacao, turno);
            if (!disponivel) {
                String diaSemana = getDiaSemanaPortugues(dataVerificacao);
                String conflito = dataVerificacao.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " (" + diaSemana + ")";
                conflitos.add(conflito);
                System.out.println("Conflito encontrado: " + conflito);
            }
        }

        resultado.put("disponivel", conflitos.isEmpty());
        if (!conflitos.isEmpty()) {
            resultado.put("mensagem", "Conflitos nas datas: " + String.join(", ", conflitos));
        }

        System.out.println("Resultado recorrente: " + (conflitos.isEmpty() ? "DISPONÍVEL" : "CONFLITO"));
        return resultado;
    }

    // Método auxiliar para obter dia da semana em português
    private String getDiaSemanaPortugues(LocalDate data) {
        String[] dias = {"Domingo", "Segunda", "Terça", "Quarta", "Quinta", "Sexta", "Sábado"};
        return dias[data.getDayOfWeek().getValue() % 7];
    }

    private boolean verificarDisponibilidadeUnica(Integer salaId, LocalDate data, Integer turno) {
        List<Agendamento> agendamentos = agendamentoService.buscarPorData(data);
        return agendamentos.stream()
                .noneMatch(a -> a.getSala().getId().equals(salaId) &&
                        a.getTurno() == turno &&
                        a.getStatus() != 2); // 2 = Reprovado
    }

    @GetMapping("/disponibilidade-edicao")
    @ResponseBody
    public Map<String, Object> verificarDisponibilidadeEdicao(
            @RequestParam Integer salaId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate data,
            @RequestParam Integer turno,
            @RequestParam Integer agendamentoId) {

        Map<String, Object> resultado = new HashMap<>();

        try {
            System.out.println("=== VERIFICANDO DISPONIBILIDADE EDIÇÃO ===");
            System.out.println("Sala ID: " + salaId + ", Data: " + data + ", Turno: " + turno + ", Agendamento ID: " + agendamentoId);

            // Busca TODOS os agendamentos para a data específica
            List<Agendamento> agendamentos = agendamentoService.buscarPorData(data);
            System.out.println("Total de agendamentos encontrados para " + data + ": " + agendamentos.size());

            boolean conflito = false;
            String mensagemConflito = "";

            for (Agendamento agendamento : agendamentos) {
                // Pula agendamentos nulos ou com dados incompletos
                if (agendamento == null || agendamento.getId() == null ||
                        agendamento.getSala() == null || agendamento.getSala().getId() == null) {
                    System.out.println("Pulando agendamento com dados incompletos");
                    continue;
                }

                System.out.println("Analisando agendamento ID: " + agendamento.getId() +
                        ", Sala: " + agendamento.getSala().getId() +
                        ", Turno: " + agendamento.getTurno() +
                        ", Status: " + agendamento.getStatus());

                // Verifica se é o próprio agendamento que está sendo editado
                boolean mesmoAgendamento = agendamento.getId().equals(agendamentoId);

                // Verifica se há conflito: mesma sala, mesmo turno, status diferente de reprovado
                boolean mesmoLocalHorario = agendamento.getSala().getId().equals(salaId) &&
                        agendamento.getTurno() == turno &&
                        agendamento.getStatus() != 2; // 2 = Reprovado

                System.out.println("Mesmo agendamento? " + mesmoAgendamento);
                System.out.println("Mesmo local/horário? " + mesmoLocalHorario);

                if (!mesmoAgendamento && mesmoLocalHorario) {
                    conflito = true;
                    mensagemConflito = "Já existe um agendamento para esta sala no turno " +
                            getTurnoDescricao(turno) + " em " +
                            data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) +
                            " (Agendamento ID: " + agendamento.getId() + ")";
                    System.out.println("*** CONFLITO ENCONTRADO: " + mensagemConflito);
                    break;
                }
            }

            resultado.put("disponivel", !conflito);
            resultado.put("mensagem", conflito ? mensagemConflito : "Sala disponível para este turno");

            System.out.println("Resultado final: " + (!conflito ? "DISPONÍVEL" : "CONFLITO"));

        } catch (Exception e) {
            System.err.println("ERRO em disponibilidade-edicao: " + e.getMessage());
            e.printStackTrace();

            resultado.put("disponivel", false);
            resultado.put("mensagem", "Erro ao verificar disponibilidade: " + e.getMessage());
        }

        return resultado;
    }

    // Método auxiliar para descrição do turno
    private String getTurnoDescricao(Integer turno) {
        switch (turno) {
            case 1: return "Manhã";
            case 2: return "Tarde";
            case 3: return "Noite";
            default: return "Desconhecido";
        }
    }

    @GetMapping("/debug-agendamentos")
    @ResponseBody
    public Map<String, Object> debugTodosAgendamentos() {
        Map<String, Object> resultado = new HashMap<>();

        try {
            List<Agendamento> todosAgendamentos = agendamentoService.listar();

            List<Map<String, Object>> agendamentosDebug = new ArrayList<>();

            for (Agendamento ag : todosAgendamentos) {
                Map<String, Object> agMap = new HashMap<>();
                agMap.put("id", ag.getId());
                agMap.put("data", ag.getData());
                agMap.put("turno", ag.getTurno());
                agMap.put("status", ag.getStatus());

                if (ag.getSala() != null) {
                    agMap.put("salaId", ag.getSala().getId());
                    agMap.put("salaBloco", ag.getSala().getBloco());
                    agMap.put("salaTipo", ag.getSala().getTipo());
                } else {
                    agMap.put("sala", "NULL");
                }

                if (ag.getUsuario() != null) {
                    agMap.put("usuarioId", ag.getUsuario().getId());
                    agMap.put("usuarioNome", ag.getUsuario().getNome());
                } else {
                    agMap.put("usuario", "NULL");
                }

                agendamentosDebug.add(agMap);
            }

            resultado.put("total", todosAgendamentos.size());
            resultado.put("agendamentos", agendamentosDebug);

        } catch (Exception e) {
            resultado.put("erro", e.getMessage());
        }

        return resultado;
    }
}
