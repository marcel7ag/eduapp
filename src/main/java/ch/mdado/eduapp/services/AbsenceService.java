package ch.mdado.eduapp.services;

import ch.mdado.eduapp.models.Absence;
import ch.mdado.eduapp.models.Student;
import ch.mdado.eduapp.models.Teacher;
import ch.mdado.eduapp.repositories.AbsenceRepository;
import ch.mdado.eduapp.repositories.StudentRepository;
import ch.mdado.eduapp.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AbsenceService {

    @Autowired
    private AbsenceRepository absenceRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    public List<Absence> findAll() {
        return absenceRepository.findAll();
    }

    public Absence findById(Integer id) {
        return absenceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Absence not found with ID: " + id));
    }

    public List<Absence> findByStudentId(Integer studentId) {
        return absenceRepository.findByStudentId(studentId);
    }

    public List<Absence> findByTeacherId(Integer teacherId) {
        return absenceRepository.findByTeacherId(teacherId);
    }

    public List<Absence> findBySubjectName(String subjectName) {
        return absenceRepository.findBySubjectName(subjectName);
    }

    public List<Absence> findUnexcusedAbsences() {
        return absenceRepository.findByIsExcused(false);
    }

    public List<Absence> findByDateRange(Date startDate, Date endDate) {
        return absenceRepository.findByAbsenceDateBetween(startDate, endDate);
    }

    public List<Absence> findTodaysAbsences() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfDay = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        Date endOfDay = cal.getTime();

        return absenceRepository.findByAbsenceDateBetween(startOfDay, endOfDay);
    }

    public List<Absence> findThisWeeksAbsences() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        Date startOfWeek = cal.getTime();

        cal.add(Calendar.WEEK_OF_YEAR, 1);
        cal.add(Calendar.MILLISECOND, -1);
        Date endOfWeek = cal.getTime();

        return absenceRepository.findByAbsenceDateBetween(startOfWeek, endOfWeek);
    }

    public Absence save(Absence absence) {
        // Validierung
        if (absence.getStudent() == null || absence.getTeacher() == null) {
            throw new IllegalArgumentException("Student and Teacher must be provided");
        }

        if (absence.getAbsenceDate() == null) {
            absence.setAbsenceDate(new Date());
        }

        return absenceRepository.save(absence);
    }

    public Absence update(Absence absence) {
        Absence existingAbsence = findById(absence.getId());
        updateData(existingAbsence, absence);
        return absenceRepository.save(existingAbsence);
    }

    private void updateData(Absence existingAbsence, Absence newAbsence) {
        if (newAbsence.getAbsenceType() != null) {
            existingAbsence.setAbsenceType(newAbsence.getAbsenceType());
        }
        if (newAbsence.getReason() != null) {
            existingAbsence.setReason(newAbsence.getReason());
        }
        if (newAbsence.getIsExcused() != null) {
            existingAbsence.setIsExcused(newAbsence.getIsExcused());
        }
        if (newAbsence.getExcuseDocument() != null) {
            existingAbsence.setExcuseDocument(newAbsence.getExcuseDocument());
        }
        existingAbsence.setUpdatedAt(new Date());
    }

    public void excuseAbsence(Integer absenceId, String excuseDocument) {
        Absence absence = findById(absenceId);
        absence.setIsExcused(true);
        absence.setExcuseDocument(excuseDocument);
        absence.setUpdatedAt(new Date());
        absenceRepository.save(absence);
    }

    public void delete(Integer id) {
        if (!absenceRepository.existsById(id)) {
            throw new RuntimeException("Absence not found with ID: " + id);
        }
        absenceRepository.deleteById(id);
    }

    public Long countUnexcusedAbsencesByStudent(Integer studentId) {
        return absenceRepository.countUnexcusedAbsencesByStudent(studentId);
    }

    public List<Absence> findRecentAbsencesByStudent(Integer studentId) {
        return absenceRepository.findByStudentIdOrderByDateDesc(studentId);
    }

    // Hilfsmethoden für Statistiken
    public long getTotalAbsencesToday() {
        return findTodaysAbsences().size();
    }

    public long getUnexcusedAbsencesToday() {
        return findTodaysAbsences().stream()
                .filter(absence -> !absence.getIsExcused())
                .count();
    }
}