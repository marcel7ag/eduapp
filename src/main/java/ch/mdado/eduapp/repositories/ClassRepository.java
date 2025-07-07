package ch.mdado.eduapp.repositories;

import ch.mdado.eduapp.models.Class;
import ch.mdado.eduapp.models.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClassRepository extends JpaRepository<Class, Integer> {

    @Transactional
    List<Class> findByTeacherId(Integer teacherId);

    @Transactional
    List<Class> findBySubjectName(String subjectName);

    @Transactional
    List<Class> findByIsActiveTrue();

    @Transactional
    List<Class> findByDayOfWeek(Integer dayOfWeek);

    @Transactional
    @Query("SELECT c FROM Class c WHERE c.teacher = :teacher AND c.isActive = true")
    List<Class> findActiveClassesByTeacher(@Param("teacher") Teacher teacher);

    @Transactional
    @Query("SELECT c FROM Class c JOIN c.students s WHERE s.id = :studentId")
    List<Class> findClassesByStudentId(@Param("studentId") Integer studentId);
}