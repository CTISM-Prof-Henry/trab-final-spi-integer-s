package br.ufsm.csi.Salas.Controller;

import br.ufsm.csi.Salas.Service.AgendamentoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/agendamento")
public class AgendamentoController {

    private final AgendamentoService agendamentoService = new AgendamentoService();

    @GetMapping
    public String listaragendamentos(@RequestParam(name = "status", required = false) Integer status, Model model) {
        if(status != null) {
            model.addAttribute("agendamentos", agendamentoService.listarPorStatus(status));
        } else {
            model.addAttribute("agendamentos", agendamentoService.listar());
        }
        return "pages/agendamentos";
    }

}
