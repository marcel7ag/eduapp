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

    }

    public Student fromDTO(StudentDTO studentDTO) {

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
