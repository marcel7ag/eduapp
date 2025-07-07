package ch.mdado.eduapp.services;

import ch.mdado.eduapp.models.*;
import ch.mdado.eduapp.models.Class;
import ch.mdado.eduapp.repositories.AbsenceRepository;
import ch.mdado.eduapp.repositories.ClassRepository;
import ch.mdado.eduapp.repositories.StudentRepository;
import ch.mdado.eduapp.repositories.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class AttendanceService {

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AbsenceRepository absenceRepository;

    /**
     * Stundenplan für einen bestimmten Wochentag abrufen
     */
    public List<Class> getScheduleForDay(int dayOfWeek) {
        return classRepository.findByDayOfWeek(dayOfWeek)
                .stream()
                .filter(Class::getIsActive)
                .sorted(Comparator.comparing(Class::getStartTime))
                .collect(Collectors.toList());
    }

    /**
     * Anwesenheitsdaten für einen bestimmten Tag und Klasse abrufen
     */
    public Map<Integer, String> getAttendanceForClass(Integer classId, Date date) {
        Class classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        // Tagesgrenzen definieren
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date startOfDay = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfDay = cal.getTime();

        // Absenzen für diesen Tag und Fach finden
        List<Absence> dayAbsences = absenceRepository.findByAbsenceDateBetween(startOfDay, endOfDay)
                .stream()
                .filter(absence -> absence.getSubjectName().equals(classEntity.getSubjectName()))
                .collect(Collectors.toList());

        // Anwesenheitsstatus für jeden Schüler ermitteln
        Map<Integer, String> attendanceStatus = new HashMap<>();

        for (Student student : classEntity.getStudents()) {
            Optional<Absence> studentAbsence = dayAbsences.stream()
                    .filter(absence -> absence.getStudent().getId().equals(student.getId()))
                    .findFirst();

            if (studentAbsence.isPresent()) {
                attendanceStatus.put(student.getId(),
                        mapAbsenceTypeToStatus(studentAbsence.get().getAbsenceType()));
            } else {
                attendanceStatus.put(student.getId(), "present");
            }
        }

        return attendanceStatus;
    }

    /**
     * Anwesenheitsdaten für eine Klasse speichern
     */
    public void saveAttendanceForClass(Integer classId, Date date, Map<Integer, String> attendanceData) {
        Class classEntity = classRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        // Bestehende Kalender-Absenzen für diesen Tag und diese Klasse löschen
        deleteExistingCalendarAbsences(classEntity, date);

        // Neue Absenzen erstellen
        for (Map.Entry<Integer, String> entry : attendanceData.entrySet()) {
            Integer studentId = entry.getKey();
            String status = entry.getValue();

            if (!"present".equals(status)) {
                Student student = studentRepository.findById(studentId)
                        .orElseThrow(() -> new RuntimeException("Student not found"));

                Absence absence = createAbsenceFromStatus(
                        student, classEntity, status, date);

                if (absence != null) {
                    absenceRepository.save(absence);
                }
            }
        }
    }

    /**
     * Bulk-Speicherung für mehrere Klassen an einem Tag
     */
    public void saveBulkAttendance(Date date, Map<String, Map<Integer, String>> bulkData) {
        for (Map.Entry<String, Map<Integer, String>> classEntry : bulkData.entrySet()) {
            String classIdStr = classEntry.getKey();
            Map<Integer, String> classAttendance = classEntry.getValue();

            try {
                Integer classId = Integer.parseInt(classIdStr);
                saveAttendanceForClass(classId, date, classAttendance);
            } catch (NumberFormatException e) {
                System.err.println("Invalid class ID: " + classIdStr);
            }
        }
    }

    /**
     * Statistiken für einen bestimmten Tag berechnen
     */
    public AttendanceStats calculateDayStats(Date date) {
        // Wochentag ermitteln
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int germanDayOfWeek = (dayOfWeek == 1) ? 7 : dayOfWeek - 1;

        // Klassen für diesen Tag
        List<Class> dayClasses = getScheduleForDay(germanDayOfWeek);

        // Absenzen für diesen Tag
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date startOfDay = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfDay = cal.getTime();

        List<Absence> dayAbsences = absenceRepository.findByAbsenceDateBetween(startOfDay, endOfDay);

        // Statistiken berechnen
        long totalStudentSlots = dayClasses.stream()
                .mapToLong(clazz -> clazz.getStudents().size())
                .sum();

        long absentCount = dayAbsences.stream()
                .filter(absence -> absence.getAbsenceType() == AbsenceType.ABSENT ||
                        absence.getAbsenceType() == AbsenceType.EXCUSED_ABSENCE)
                .count();

        long lateCount = dayAbsences.stream()
                .filter(absence -> absence.getAbsenceType() == AbsenceType.LATE ||
                        absence.getAbsenceType() == AbsenceType.EARLY_LEAVE)
                .count();

        long presentCount = totalStudentSlots - dayAbsences.size();

        return new AttendanceStats(
                dayClasses.size(),
                totalStudentSlots,
                presentCount,
                absentCount,
                lateCount
        );
    }

    /**
     * Wochenübersicht der Anwesenheit
     */
    public Map<Date, AttendanceStats> getWeeklyAttendance(Date weekStart) {
        Map<Date, AttendanceStats> weeklyStats = new LinkedHashMap<>();

        Calendar cal = Calendar.getInstance();
        cal.setTime(weekStart);

        // Auf Montag setzen
        while (cal.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
            cal.add(Calendar.DAY_OF_MONTH, -1);
        }

        // 7 Tage durchgehen
        for (int i = 0; i < 7; i++) {
            Date day = cal.getTime();
            AttendanceStats stats = calculateDayStats(day);
            weeklyStats.put(new Date(day.getTime()), stats);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }

        return weeklyStats;
    }

    // Helper Methods

    private String mapAbsenceTypeToStatus(AbsenceType absenceType) {
        return switch (absenceType) {
            case LATE -> "late";
            case ABSENT -> "absent";
            case EARLY_LEAVE -> "early";
            case EXCUSED_ABSENCE -> "absent";
        };
    }

    private Absence createAbsenceFromStatus(Student student, Class classEntity, String status, Date date) {
        AbsenceType absenceType = switch (status) {
            case "late" -> AbsenceType.LATE;
            case "absent" -> AbsenceType.ABSENT;
            case "early" -> AbsenceType.EARLY_LEAVE;
            default -> AbsenceType.ABSENT;
        };

        String reason = switch (status) {
            case "late" -> "Verspätung (Kalender-Erfassung)";
            case "absent" -> "Abwesend (Kalender-Erfassung)";
            case "early" -> "Früh gegangen (Kalender-Erfassung)";
            default -> "Kalender-Erfassung";
        };

        // Datum mit Unterrichtszeit kombinieren
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        Calendar timeCal = Calendar.getInstance();
        timeCal.setTime(classEntity.getStartTime());

        cal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
        cal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));

        return new Absence(student, classEntity.getTeacher(), classEntity,
                cal.getTime(), absenceType, reason);
    }

    private void deleteExistingCalendarAbsences(Class classEntity, Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        Date startOfDay = cal.getTime();

        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        Date endOfDay = cal.getTime();

        List<Absence> existingAbsences = absenceRepository.findByAbsenceDateBetween(startOfDay, endOfDay)
                .stream()
                .filter(absence -> absence.getSubjectName().equals(classEntity.getSubjectName()))
                .filter(absence -> absence.getReason() != null &&
                        absence.getReason().contains("Kalender-Erfassung"))
                .collect(Collectors.toList());

        for (Absence absence : existingAbsences) {
            absenceRepository.delete(absence);
        }
    }

    // Stats DTO
    public static class AttendanceStats {
        private final long totalClasses;
        private final long totalStudentSlots;
        private final long presentCount;
        private final long absentCount;
        private final long lateCount;

        public AttendanceStats(long totalClasses, long totalStudentSlots,
                               long presentCount, long absentCount, long lateCount) {
            this.totalClasses = totalClasses;
            this.totalStudentSlots = totalStudentSlots;
            this.presentCount = presentCount;
            this.absentCount = absentCount;
            this.lateCount = lateCount;
        }

        // Getters
        public long getTotalClasses() { return totalClasses; }
        public long getTotalStudentSlots() { return totalStudentSlots; }
        public long getPresentCount() { return presentCount; }
        public long getAbsentCount() { return absentCount; }
        public long getLateCount() { return lateCount; }

        public double getAttendanceRate() {
            return totalStudentSlots > 0 ? (double) presentCount / totalStudentSlots * 100 : 0;
        }
    }
}