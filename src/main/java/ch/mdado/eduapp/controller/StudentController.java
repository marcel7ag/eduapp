package ch.mdado.eduapp.controller;

import ch.mdado.eduapp.dto.StudentDTO;
import ch.mdado.eduapp.models.Student;
import ch.mdado.eduapp.services.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/students")
public class StudentController {

    @Autowired
    private StudentService service;

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<Student> findById(@PathVariable Integer id){
        Student student = service.findById(id);
        return ResponseEntity.ok().body(student);
    }



    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Void> update(@Valid @RequestBody StudentDTO studentDTO, @PathVariable Integer id) {
        Student student = service.fromDTO(studentDTO);
        student.setId(id);
        student = service.update(student);
        return ResponseEntity.noContent().build();
    }
}
