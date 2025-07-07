package ch.mdado.eduapp.repositories;

import ch.mdado.eduapp.models.Class;
import ch.mdado.eduapp.models.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClassRepository extends JpaRepository<Class, Integer> {

    @Transactional
    @Query("SELECT c FROM Class c LEFT JOIN FETCH c.students LEFT JOIN FETCH c.teacher")
    List<Class> findAll();

    @Transactional
    @Query("SELECT c FROM Class c LEFT JOIN FETCH c.students LEFT JOIN FETCH c.teacher WHERE c.id = :id")
    Optional<Class> findById(@Param("id") Integer id);

    @Transactional
    @Query("SELECT c FROM Class c LEFT JOIN FETCH c.students LEFT JOIN FETCH c.teacher WHERE c.teacher.id = :teacherId")
    List<Class> findByTeacherId(@Param("teacherId") Integer teacherId);

    @Transactional
    @Query("SELECT c FROM Class c LEFT JOIN FETCH c.students LEFT JOIN FETCH c.teacher WHERE c.subjectName = :subjectName")
    List<Class> findBySubjectName(@Param("subjectName") String subjectName);

    @Transactional
    @Query("SELECT c FROM Class c LEFT JOIN FETCH c.students LEFT JOIN FETCH c.teacher WHERE c.isActive = true")
    List<Class> findByIsActiveTrue();

    @Transactional
    @Query("SELECT c FROM Class c LEFT JOIN FETCH c.students LEFT JOIN FETCH c.teacher WHERE c.dayOfWeek = :dayOfWeek")
    List<Class> findByDayOfWeek(@Param("dayOfWeek") Integer dayOfWeek);

    @Transactional
    @Query("SELECT c FROM Class c LEFT JOIN FETCH c.students LEFT JOIN FETCH c.teacher WHERE c.teacher = :teacher AND c.isActive = true")
    List<Class> findActiveClassesByTeacher(@Param("teacher") Teacher teacher);

    @Transactional
    @Query("SELECT c FROM Class c LEFT JOIN FETCH c.students s LEFT JOIN FETCH c.teacher WHERE s.id = :studentId")
    List<Class> findClassesByStudentId(@Param("studentId") Integer studentId);

    // Zusätzliche Abfrage um die Beziehungen zu überprüfen
    @Transactional
    @Query("SELECT COUNT(s) FROM Class c JOIN c.students s WHERE c.id = :classId")
    Long countStudentsInClass(@Param("classId") Integer classId);

    // Debug-Abfrage
    @Transactional
    @Query("SELECT c.id, c.className, c.subjectName, COUNT(s) FROM Class c LEFT JOIN c.students s GROUP BY c.id, c.className, c.subjectName")
    List<Object[]> findClassesWithStudentCount();
}