package ch.mdado.eduapp.controller;

import ch.mdado.eduapp.dto.StudentDTO;
import ch.mdado.eduapp.models.Student;
import ch.mdado.eduapp.services.StudentService;
import ch.mdado.eduapp.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping(value = "/students")
public class StudentController {

    @Autowired
    private StudentService service;

    @Autowired
    private StudentRepository studentRepository;

    // Web-Endpoints für Thymeleaf Views
    @GetMapping
    public String listStudents(Model model) {
        List<Student> students = studentRepository.findAll();
        model.addAttribute("students", students);
        return "students";
    }

    @GetMapping("/web/{id}")
    public String studentDetails(@PathVariable Integer id, Model model) {
        Student student = service.findById(id);
        model.addAttribute("student", student);
        return "student-detail";
    }

    // REST API Endpoints
    @GetMapping("/api")
    @ResponseBody
    public ResponseEntity<List<Student>> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Student> findById(@PathVariable Integer id) {
        Student student = service.findById(id);
        return ResponseEntity.ok().body(student);
    }

    @PutMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> update(@Valid @RequestBody StudentDTO studentDTO, @PathVariable Integer id) {
        Student student = service.fromDTO(studentDTO);
        student.setId(id);
        student = service.update(student);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api")
    @ResponseBody
    public ResponseEntity<Student> create(@Valid @RequestBody StudentDTO studentDTO) {
        Student student = service.fromDTO(studentDTO);
        student = studentRepository.save(student);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/api/{id}")
    @ResponseBody
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!studentRepository.existsById(id)) {
            throw new RuntimeException("Student not found with ID: " + id);
        }
        studentRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}