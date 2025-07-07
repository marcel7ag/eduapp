package ch.mdado.eduapp.controller;

import ch.mdado.eduapp.models.Absence;
import ch.mdado.eduapp.services.AbsenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/absences")
public class AbsenceController {

    @Autowired
    private AbsenceService absenceService;

    // Web-Endpoints für Thymeleaf Views
    @GetMapping
    public String listAbsences(Model model) {
        List<Absence> absences = absenceService.findAll();
        model.addAttribute("absences", absences);
        model.addAttribute("totalAbsencesToday", absenceService.getTotalAbsencesToday());
        model.addAttribute("unexcusedAbsencesToday", absenceService.getUnexcusedAbsencesToday());
        return "absences";
    }

    @GetMapping("/today")
    public String todaysAbsences(Model model) {
        List<Absence> todaysAbsences = absenceService.findTodaysAbsences();
        model.addAttribute("absences", todaysAbsences);
        model.addAttribute("pageTitle", "Heutige Absenzen");
        return "absences";
    }

    @GetMapping("/week")
    public String thisWeeksAbsences(Model model) {
        List<Absence> weekAbsences = absenceService.findThisWeeksAbsences();
        model.addAttribute("absences", weekAbsences);
        model.addAttribute("pageTitle", "Absenzen dieser Woche");
        return "absences";
    }

    @GetMapping("/unexcused")
    public String unexcusedAbsences(Model model) {
        List<Absence> unexcusedAbsences = absenceService.findUnexcusedAbsences();
        model.addAttribute("absences", unexcusedAbsences);
        model.addAttribute("pageTitle", "Unentschuldigte Absenzen");
        return "absences";
    }

    @GetMapping("/student/{studentId}")
    public String studentAbsences(@PathVariable Integer studentId, Model model) {
        List<Absence> studentAbsences = absenceService.findByStudentId(studentId);
        Long unexcusedCount = absenceService.countUnexcusedAbsencesByStudent(studentId);

        model.addAttribute("absences", studentAbsences);
        model.addAttribute("unexcusedCount", unexcusedCount);
        model.addAttribute("pageTitle", "Absenzen des Schülers");
        return "absences";
    }

    @GetMapping("/new")
    public String newAbsenceForm(Model model) {
        model.addAttribute("absence", new Absence());
        return "absence-form";
    }

    @PostMapping("/excuse/{id}")
    public String excuseAbsence(@PathVariable Integer id,
                                @RequestParam(required = false) String excuseDocument) {
        absenceService.excuseAbsence(id, excuseDocument);
        return "redirect:/absences";
    }

    // REST API Endpoints
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Absence>> getAllAbsences() {
        List<Absence> absences = absenceService.findAll();
        return ResponseEntity.ok(absences);
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Absence> getAbsenceById(@PathVariable Integer id) {
        Absence absence = absenceService.findById(id);
        return ResponseEntity.ok(absence);
    }

    @GetMapping("/api/student/{studentId}")
    @ResponseBody
    public ResponseEntity<List<Absence>> getAbsencesByStudent(@PathVariable Integer studentId) {
        List<Absence> absences = absenceService.findByStudentId(studentId);
        return ResponseEntity.ok(absences);
    }

    @GetMapping("/api/teacher/{teacherId}")
    @ResponseBody
    public ResponseEntity<List<Absence>> getAbsencesByTeacher(@PathVariable Integer teacherId) {
        List<Absence> absences = absenceService.findByTeacherId(teacherId);
        return ResponseEntity.ok(absences);
    }

    @GetMapping("/api/subject/{subjectName}")
    @ResponseBody
    public ResponseEntity<List<Absence>> getAbsencesBySubject(@PathVariable String subjectName) {
        List<Absence> absences = absenceService.findBySubjectName(subjectName);
        return ResponseEntity.ok(absences);
    }

    @GetMapping("/api/date-range")
    @ResponseBody
    public ResponseEntity<List<Absence>> getAbsencesByDateRange(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<Absence> absences = absenceService.findByDateRange(startDate, endDate);
        return ResponseEntity.ok(absences);
    }

    @GetMapping("/api/today")
    @ResponseBody
    public ResponseEntity<List<Absence>> getTodaysAbsences() {
        List<Absence> absences = absenceService.findTodaysAbsences();
        return ResponseEntity.ok(absences);
    }

    @GetMapping("/api/week")
    @ResponseBody
    public ResponseEntity<List<Absence>> getThisWeeksAbsences() {
        List<Absence> absences = absenceService.findThisWeeksAbsences();
        return ResponseEntity.ok(absences);
    }

    @GetMapping("/api/unexcused")
    @ResponseBody
    public ResponseEntity<List<Absence>> getUnexcusedAbsences() {
        List<Absence> absences = absenceService.findUnexcusedAbsences();
        return ResponseEntity.ok(absences);
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Absence> createAbsence(@RequestBody Absence absence) {
        Absence savedAbsence = absenceService.save(absence);
        return ResponseEntity.ok(savedAbsence);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Absence> updateAbsence(@PathVariable Integer id,
                                                 @RequestBody Absence absence) {
        absence.setId(id);
        Absence updatedAbsence = absenceService.update(absence);
        return ResponseEntity.ok(updatedAbsence);
    }

    @PutMapping("/api/{id}/excuse")
    @ResponseBody
    public ResponseEntity<Void> excuseAbsence(@PathVariable Integer id,
                                              @RequestBody Map<String, String> request) {
        String excuseDocument = request.get("excuseDocument");
        absenceService.excuseAbsence(id, excuseDocument);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> deleteAbsence(@PathVariable Integer id) {
        absenceService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/api/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAbsenceStats() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAbsencesToday", absenceService.getTotalAbsencesToday());
        stats.put("unexcusedAbsencesToday", absenceService.getUnexcusedAbsencesToday());
        stats.put("totalAbsencesThisWeek", absenceService.findThisWeeksAbsences().size());
        stats.put("totalUnexcusedAbsences", absenceService.findUnexcusedAbsences().size());

        return ResponseEntity.ok(stats);
    }

    @GetMapping("/api/student/{studentId}/stats")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getStudentAbsenceStats(@PathVariable Integer studentId) {
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalAbsences", absenceService.findByStudentId(studentId).size());
        stats.put("unexcusedAbsences", absenceService.countUnexcusedAbsencesByStudent(studentId));

        return ResponseEntity.ok(stats);
    }
}