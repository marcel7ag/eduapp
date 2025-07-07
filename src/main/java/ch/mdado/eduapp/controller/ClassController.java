package ch.mdado.eduapp.controller;

import ch.mdado.eduapp.models.Class;
import ch.mdado.eduapp.repositories.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/classes")
public class ClassController {

    @Autowired
    private ClassRepository classRepository;

    @GetMapping
    public String listClasses(Model model) {
        List<Class> classes = classRepository.findAll();
        model.addAttribute("classes", classes);
        return "classes";
    }

    @GetMapping("/{id}")
    public String classDetails(@PathVariable Integer id, Model model) {
        Class classEntity = classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found"));
        model.addAttribute("class", classEntity);
        return "class-detail";
    }

    // REST API
    @GetMapping("/api")
    @ResponseBody
    public List<Class> getAllClasses() {
        return classRepository.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Class getClassById(@PathVariable Integer id) {
        return classRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Class not found"));
    }
}