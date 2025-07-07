package ch.mdado.eduapp.services;

import ch.mdado.eduapp.dto.StudentDTO;
import ch.mdado.eduapp.models.Student;
import ch.mdado.eduapp.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    public Student findById(Integer id) {
        return repository.findById(id)
            .orElseThrow(() -> new RuntimeException("Student not found with ID: " + id));
    }


    public Student fromDTO(StudentDTO dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setName(dto.getName());
        student.setEmail(dto.getEmail());
        return student;
    }
    public Student update(Student student) {
        Student newStudent = findById(student.getId());
        updateData(newStudent, student);
        return repository.save(newStudent);
    }

    private void updateData(Student newStudent, Student student) {
        newStudent.setName(student.getName());
        newStudent.setEmail(student.getEmail());
    }
}
