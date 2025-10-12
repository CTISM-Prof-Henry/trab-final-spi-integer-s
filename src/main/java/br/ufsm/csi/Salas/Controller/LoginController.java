package br.ufsm.csi.Salas.Controller;

import br.ufsm.csi.Salas.Service.LoginService;
import br.ufsm.csi.Salas.model.Funcionario;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class LoginController {

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @PostMapping("/login")
    public String login(HttpSession session, Model model, String email, String senha) {
        Funcionario funcionario = new LoginService().autenticar(email, senha);

        if(funcionario != null) {
            session.setAttribute("funcionarioLogado", funcionario);
            return "redirect:/home";
        } else {
            model.addAttribute("msg", "Login ou senha incorreto!");
            return "index";
        }
    }

    @GetMapping("/home")
    public String home() {
        return "pages/home";

    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

}

