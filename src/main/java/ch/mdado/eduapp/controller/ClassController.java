package ch.mdado.eduapp.controller;

import ch.mdado.eduapp.models.Class;
import ch.mdado.eduapp.models.Student;
import ch.mdado.eduapp.services.ClassService;
import ch.mdado.eduapp.repositories.ClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/classes")
public class ClassController {

    @Autowired
    private ClassService classService;

    @Autowired
    private ClassRepository classRepository;

    @GetMapping
    public String listClasses(Model model) {
        List<Class> classes = classService.findAll();
        System.out.println("=== DEBUG: Classes loaded ===");
        for (Class c : classes) {
            System.out.println("Class: " + c.getClassName() + " - " + c.getSubjectName());
            System.out.println("Students count: " + c.getStudents().size());
            for (Student s : c.getStudents()) {
                System.out.println("  - Student: " + s.getName());
            }
        }
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
    public ResponseEntity<Map<String, Object>> getClassById(@PathVariable Integer id) {
        Class classEntity = classService.findById(id);

        System.out.println("=== DEBUG: Class Details ===");
        System.out.println("Class ID: " + classEntity.getId());
        System.out.println("Class Name: " + classEntity.getClassName());
        System.out.println("Subject: " + classEntity.getSubjectName());
        System.out.println("Students in class: " + classEntity.getStudents().size());

        // Zusätzliche Debug-Abfrage
        Long studentCount = classRepository.countStudentsInClass(id);
        System.out.println("Student count from repository: " + studentCount);

        // Manuell ein DTO-ähnliches Map-Objekt erstellen
        Map<String, Object> classData = new HashMap<>();
        classData.put("id", classEntity.getId());
        classData.put("className", classEntity.getClassName());
        classData.put("subjectName", classEntity.getSubjectName());
        classData.put("startTime", classEntity.getStartTime());
        classData.put("endTime", classEntity.getEndTime());
        classData.put("dayOfWeek", classEntity.getDayOfWeek());
        classData.put("dayOfWeekName", classEntity.getDayOfWeekName());
        classData.put("roomNumber", classEntity.getRoomNumber());
        classData.put("maxStudents", classEntity.getMaxStudents());
        classData.put("isActive", classEntity.getIsActive());
        classData.put("semesterStart", classEntity.getSemesterStart());
        classData.put("semesterEnd", classEntity.getSemesterEnd());

        // Teacher-Informationen
        Map<String, Object> teacherData = new HashMap<>();
        teacherData.put("id", classEntity.getTeacher().getId());
        teacherData.put("name", classEntity.getTeacher().getName());
        teacherData.put("email", classEntity.getTeacher().getEmail());
        classData.put("teacher", teacherData);

        // Student-Informationen
        List<Map<String, Object>> studentsData = classEntity.getStudents().stream()
                .map(student -> {
                    Map<String, Object> studentData = new HashMap<>();
                    studentData.put("id", student.getId());
                    studentData.put("name", student.getName());
                    studentData.put("email", student.getEmail());
                    System.out.println("  - Adding student: " + student.getName());
                    return studentData;
                })
                .collect(Collectors.toList());
        classData.put("students", studentsData);

        System.out.println("Final students data size: " + studentsData.size());

        return ResponseEntity.ok(classData);
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

    // Debug-Endpoint
    @GetMapping("/api/debug")
    @ResponseBody
    public ResponseEntity<List<Object[]>> getDebugInfo() {
        List<Object[]> debugInfo = classRepository.findClassesWithStudentCount();
        return ResponseEntity.ok(debugInfo);
    }
}