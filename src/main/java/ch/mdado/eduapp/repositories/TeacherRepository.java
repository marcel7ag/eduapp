package ch.mdado.eduapp.repositories;

import ch.mdado.eduapp.models.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    @Transactional
    Teacher findByEmail(String email);
}
