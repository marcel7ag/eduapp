package ch.mdado.eduapp.repositories;

import ch.mdado.eduapp.models.Student;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    @Transactional
    Student findByEmail(String email);

    @Transactional
    Optional<Student> findById(Integer id);
}
