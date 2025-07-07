package ch.mdado.eduapp.repositories;

import ch.mdado.eduapp.models.Absence;
import ch.mdado.eduapp.models.Student;
import ch.mdado.eduapp.models.Teacher;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AbsenceRepository extends JpaRepository<Absence, Integer> {

    @Transactional
    List<Absence> findByStudentId(Integer studentId);

    @Transactional
    List<Absence> findByTeacherId(Integer teacherId);

    @Transactional
    List<Absence> findBySubjectName(String subjectName);

    @Transactional
    List<Absence> findByIsExcused(Boolean isExcused);

    @Transactional
    List<Absence> findByAbsenceDateBetween(Date startDate, Date endDate);

    @Transactional
    @Query("SELECT a FROM Absence a WHERE a.student = :student AND a.absenceDate BETWEEN :startDate AND :endDate")
    List<Absence> findByStudentAndDateRange(@Param("student") Student student,
                                            @Param("startDate") Date startDate,
                                            @Param("endDate") Date endDate);

    @Transactional
    @Query("SELECT a FROM Absence a WHERE a.teacher = :teacher AND a.absenceDate BETWEEN :startDate AND :endDate")
    List<Absence> findByTeacherAndDateRange(@Param("teacher") Teacher teacher,
                                            @Param("startDate") Date startDate,
                                            @Param("endDate") Date endDate);

    @Transactional
    @Query("SELECT COUNT(a) FROM Absence a WHERE a.student.id = :studentId AND a.isExcused = false")
    Long countUnexcusedAbsencesByStudent(@Param("studentId") Integer studentId);

    @Transactional
    @Query("SELECT a FROM Absence a WHERE a.student.id = :studentId ORDER BY a.absenceDate DESC")
    List<Absence> findByStudentIdOrderByDateDesc(@Param("studentId") Integer studentId);
}