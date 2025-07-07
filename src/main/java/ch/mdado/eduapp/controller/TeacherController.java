package ch.mdado.eduapp.controller;

import ch.mdado.eduapp.models.Teacher;
import ch.mdado.eduapp.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/teachers")
public class TeacherController {

    @Autowired
    private TeacherRepository teacherRepository;

    @GetMapping
    public String listTeachers(Model model) {
        List<Teacher> teachers = teacherRepository.findAll();
        model.addAttribute("teachers", teachers);
        return "teachers";
    }

    @GetMapping("/{id}")
    public String teacherDetails(@PathVariable Integer id, Model model) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
        model.addAttribute("teacher", teacher);
        return "teacher-detail";
    }

    // REST API
    @GetMapping("/api")
    @ResponseBody
    public List<Teacher> getAllTeachers() {
        return teacherRepository.findAll();
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public Teacher getTeacherById(@PathVariable Integer id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Teacher not found"));
    }
}