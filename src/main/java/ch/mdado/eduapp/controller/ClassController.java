package ch.mdado.eduapp.controller;

import ch.mdado.eduapp.models.Class;
import ch.mdado.eduapp.services.ClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    @GetMapping
    public String listClasses(Model model) {
        List<Class> classes = classService.findAll();
        model.addAttribute("classes", classes);
        return "classes";
    }

    @GetMapping("/{id}")
    public String classDetails(@PathVariable Integer id, Model model) {
        Class classEntity = classService.findById(id);
        model.addAttribute("class", classEntity);
        return "class-detail";
    }

    // REST API
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Class>> getAllClasses() {
        List<Class> classes = classService.findAll();
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Class> getClassById(@PathVariable Integer id) {
        Class classEntity = classService.findById(id);
        return ResponseEntity.ok(classEntity);
    }

    @GetMapping("/api/teacher/{teacherId}")
    @ResponseBody
    public ResponseEntity<List<Class>> getClassesByTeacher(@PathVariable Integer teacherId) {
        List<Class> classes = classService.findByTeacherId(teacherId);
        return ResponseEntity.ok(classes);
    }

    @GetMapping("/api/student/{studentId}")
    @ResponseBody
    public ResponseEntity<List<Class>> getClassesByStudent(@PathVariable Integer studentId) {
        List<Class> classes = classService.findByStudentId(studentId);
        return ResponseEntity.ok(classes);
    }
}