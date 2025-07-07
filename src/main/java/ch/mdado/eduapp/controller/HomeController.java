package ch.mdado.eduapp.controller;

import ch.mdado.eduapp.services.AbsenceService;
import ch.mdado.eduapp.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private AbsenceService absenceService;

    @Autowired
    private StudentService studentService;

    @GetMapping("/")
    public String home(Model model) {
        // Dashboard-Statistiken
        model.addAttribute("totalAbsencesToday", absenceService.getTotalAbsencesToday());
        model.addAttribute("unexcusedAbsencesToday", absenceService.getUnexcusedAbsencesToday());
        model.addAttribute("totalAbsencesWeek", absenceService.findThisWeeksAbsences().size());
        model.addAttribute("recentAbsences", absenceService.findAll().stream().limit(5).toList());

        return "home";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return home(model);
    }
}