package br.ufsm.csi.salas.controller;

import br.ufsm.csi.salas.model.Agendamento;
import br.ufsm.csi.salas.service.AgendamentoService;
import br.ufsm.csi.salas.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping({"/home"})
public class HomeController {

    private final DashboardService dashboardService;
    private final AgendamentoService agendamentoService;

    public HomeController(DashboardService dashboardService, AgendamentoService agendamentoService) {
        this.dashboardService = dashboardService;
        this.agendamentoService = agendamentoService;
    }

    @GetMapping
    public String home(Model model,
                       @RequestParam(name = "year", required = false) Integer year,
                       @RequestParam(name = "month", required = false) Integer month) {

        model.addAttribute("salasEmUso", dashboardService.getSalasEmUso());
        model.addAttribute("salasLivres", dashboardService.getSalasLivres());
        model.addAttribute("agendamentosHoje", dashboardService.getAgendamentosHoje());
        model.addAttribute("turnoAtual", dashboardService.getNomeTurnoAtual());

        YearMonth ym = (year != null && month != null)
                ? YearMonth.of(year, month)
                : YearMonth.now();

        LocalDate firstOfMonth = ym.atDay(1);
        int shift = firstOfMonth.getDayOfWeek().getValue() % 7; // domingo=0
        LocalDate gridStart = firstOfMonth.minusDays(shift);

        List<Agendamento> todos = agendamentoService.listar();
        Map<LocalDate, List<Agendamento>> porData = todos.stream()
                .filter(a -> a.getData() != null)
                .collect(Collectors.groupingBy(Agendamento::getData));

        List<Map<String, Object>> calendarCells = new ArrayList<>(42);
        DateTimeFormatter dayFmt = DateTimeFormatter.ofPattern("d");

        for (int i = 0; i < 42; i++) {
            LocalDate cellDate = gridStart.plusDays(i);
            boolean otherMonth = !cellDate.getMonth().equals(ym.getMonth());
            List<Agendamento> ags = porData.getOrDefault(cellDate, Collections.emptyList());

            List<CalendarItem> items = ags.stream().map(a -> {
                String salaText = "Sala";
                if (a.getSala() != null) {
                    salaText = String.format("%s - Bloco %s",
                            a.getSala().getTipo() == null ? "Sala" : a.getSala().getTipo(),
                            a.getSala().getBloco() == null ? "" : a.getSala().getBloco());
                }

                String usuario = (a.getUsuario() != null && a.getUsuario().getNome() != null)
                        ? a.getUsuario().getNome()
                        : "—";

                String descricao = String.format("%s (%s) — %s",
                        salaText,
                        a.getTurno() == 1 ? "Manhã" : a.getTurno() == 2 ? "Tarde" : "Noite",
                        usuario);

                return new CalendarItem(a.getId() , descricao, a.getTurno());
            }).collect(Collectors.toList());

            Map<String, Object> cell = new HashMap<>();
            cell.put("date", cellDate);
            cell.put("otherMonth", otherMonth);
            cell.put("dayNumber", dayFmt.format(cellDate));
            cell.put("items", items);
            cell.put("visibleItems", items.stream().limit(3).collect(Collectors.toList()));
            cell.put("moreCount", Math.max(0, items.size() - 3));

            calendarCells.add(cell);
        }

        model.addAttribute("calendarCells", calendarCells);
        model.addAttribute("displayMonth", ym.getMonth().getDisplayName(java.time.format.TextStyle.FULL, new Locale("pt", "BR")));
        model.addAttribute("displayYear", ym.getYear());
        model.addAttribute("year", ym.getYear());
        model.addAttribute("month", ym.getMonthValue());

        return "pages/home";
    }

    public static class CalendarItem {
        private final Integer id;
        private final String descricao;
        private final int turno;

        public CalendarItem(Integer id, String descricao, int turno) {
            this.id = id;
            this.descricao = descricao;
            this.turno = turno;
        }

        public Integer getId() {
            return id;
        }

        public String getDescricao() {
            return descricao;
        }

        public int getTurno() {
            return turno;
        }
    }
}
