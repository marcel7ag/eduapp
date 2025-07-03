package ch.mdado.eduapp.repositories;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {
    @Transactional
    Teacher findByEmail(String email);
}
