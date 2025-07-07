package ch.mdado.eduapp.controller;

import ch.mdado.eduapp.models.*;
import ch.mdado.eduapp.models.Class;
import ch.mdado.eduapp.services.AttendanceService;
import ch.mdado.eduapp.services.ClassService;
import ch.mdado.eduapp.services.AbsenceService;
import ch.mdado.eduapp.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired
    private ClassService classService;

    @Autowired
    private AbsenceService absenceService;

    @Autowired
    private StudentRepository studentRepository;

    // Web-Endpoint für Thymeleaf View
    @GetMapping
    public String attendanceCalendar(Model model) {
        return "attendance";
    }

    // REST API Endpoints

    /**
     * Stundenplan für einen bestimmten Tag abrufen
     */
    @GetMapping("/api/schedule/{date}")
    @ResponseBody
    public ResponseEntity<List<Map<String, Object>>> getDaySchedule(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

        // Wochentag ermitteln (1 = Montag, 7 = Sonntag)
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);

        // Sonntag von 1 auf 7 umrechnen, Montag = 1
        int germanDayOfWeek = (dayOfWeek == 1) ? 7 : dayOfWeek - 1;

        // Klassen für diesen Wochentag abrufen
        List<Class> classes = classService.findByDayOfWeek(germanDayOfWeek);

        // Zu JSON-Format konvertieren
        List<Map<String, Object>> schedule = classes.stream()
                .map(this::convertClassToScheduleItem)
                .collect(Collectors.toList());

        return ResponseEntity.ok(schedule);
    }

    /**
     * Schülerliste für eine bestimmte Klasse abrufen
     */
    @GetMapping("/api/students/{classId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getClassStudents(@PathVariable Integer classId) {
        Class classEntity = classService.findById(classId);

        Map<String, Object> response = new HashMap<>();
        response.put("classInfo", convertClassToScheduleItem(classEntity));

        // Schüler zu kompaktem Format konvertieren
        List<Map<String, Object>> students = classEntity.getStudents().stream()
                .map(student -> {
                    Map<String, Object> studentData = new HashMap<>();
                    studentData.put("id", student.getId());
                    studentData.put("name", student.getName());
                    studentData.put("email", student.getEmail());
                    return studentData;
                })
                .collect(Collectors.toList());

        response.put("students", students);
        return ResponseEntity.ok(response);
    }

    /**
     * Anwesenheitsdaten für einen bestimmten Tag und Klasse abrufen
     */
    @GetMapping("/api/attendance/{classId}/{date}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getAttendanceData(
            @PathVariable Integer classId,
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

        Class classEntity = classService.findById(classId);

        // Absenzen für diesen Tag und diese Klasse finden
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

        List<Absence> dayAbsences = absenceService.findByDateRange(startOfDay, endOfDay)
                .stream()
                .filter(absence -> absence.getSubjectName().equals(classEntity.getSubjectName()))
                .collect(Collectors.toList());

        // Anwesenheitsdaten zusammenstellen
        Map<String, Object> attendanceData = new HashMap<>();
        for (Student student : classEntity.getStudents()) {
            String studentKey = classId + "-" + student.getId();

            // Prüfen ob Schüler Absenz hat
            Optional<Absence> studentAbsence = dayAbsences.stream()
                    .filter(absence -> absence.getStudent().getId().equals(student.getId()))
                    .findFirst();

            if (studentAbsence.isPresent()) {
                attendanceData.put(studentKey, mapAbsenceTypeToStatus(studentAbsence.get().getAbsenceType()));
            } else {
                attendanceData.put(studentKey, "present");
            }
        }

        return ResponseEntity.ok(attendanceData);
    }

    /**
     * Anwesenheitsdaten speichern
     */
    @PostMapping("/api/save")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> saveAttendance(@RequestBody AttendanceRequest request) {
        try {
            Date attendanceDate = request.getDate();
            Map<String, String> attendanceData = request.getAttendanceData();

            // Bestehende Absenzen für diesen Tag löschen
            deleteExistingAbsences(attendanceDate);

            // Neue Absenzen erstellen
            List<Absence> newAbsences = new ArrayList<>();

            for (Map.Entry<String, String> entry : attendanceData.entrySet()) {
                String key = entry.getKey(); // Format: "classId-studentId"
                String status = entry.getValue();

                if (!"present".equals(status)) {
                    String[] parts = key.split("-");
                    if (parts.length >= 2) {
                        Integer classId = Integer.parseInt(parts[0]);
                        Integer studentId = Integer.parseInt(parts[1]);

                        Absence absence = createAbsenceFromStatus(classId, studentId, status, attendanceDate);
                        if (absence != null) {
                            newAbsences.add(absence);
                        }
                    }
                }
            }

            // Alle neuen Absenzen speichern
            for (Absence absence : newAbsences) {
                absenceService.save(absence);
            }

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Anwesenheitsdaten erfolgreich gespeichert");
            response.put("savedAbsences", newAbsences.size());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Fehler beim Speichern: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    /**
     * Statistiken für einen bestimmten Tag abrufen
     */
    @GetMapping("/api/stats/{date}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getDayStats(
            @PathVariable @DateTimeFormat(pattern = "yyyy-MM-dd") Date date) {

        // Wochentag ermitteln
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int germanDayOfWeek = (dayOfWeek == 1) ? 7 : dayOfWeek - 1;

        // Klassen für diesen Tag
        List<Class> dayClasses = classService.findByDayOfWeek(germanDayOfWeek);

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

        List<Absence> dayAbsences = absenceService.findByDateRange(startOfDay, endOfDay);

        // Statistiken berechnen
        long totalStudents = dayClasses.stream()
                .mapToLong(clazz -> clazz.getStudents().size())
                .sum();

        long absentCount = dayAbsences.stream()
                .filter(absence -> absence.getAbsenceType() == AbsenceType.ABSENT)
                .count();

        long lateCount = dayAbsences.stream()
                .filter(absence -> absence.getAbsenceType() == AbsenceType.LATE ||
                        absence.getAbsenceType() == AbsenceType.EARLY_LEAVE)
                .count();

        Map<String, Object> stats = new HashMap<>();
        stats.put("totalClasses", dayClasses.size());
        stats.put("totalStudents", totalStudents);
        stats.put("presentCount", totalStudents - dayAbsences.size());
        stats.put("absentCount", absentCount);
        stats.put("lateCount", lateCount);

        return ResponseEntity.ok(stats);
    }

    // Helper Methods

    private Map<String, Object> convertClassToScheduleItem(Class classEntity) {
        Map<String, Object> item = new HashMap<>();
        item.put("id", classEntity.getId());
        item.put("subject", classEntity.getSubjectName());
        item.put("class", classEntity.getClassName());
        item.put("teacher", classEntity.getTeacher().getName());
        item.put("room", classEntity.getRoomNumber());
        item.put("students", classEntity.getStudents().size());

        // Zeit formatieren
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String startTime = timeFormat.format(classEntity.getStartTime());
        String endTime = timeFormat.format(classEntity.getEndTime());
        item.put("time", startTime + "-" + endTime);
        item.put("startTime", startTime);
        item.put("endTime", endTime);

        return item;
    }

    private String mapAbsenceTypeToStatus(AbsenceType absenceType) {
        return switch (absenceType) {
            case LATE -> "late";
            case ABSENT -> "absent";
            case EARLY_LEAVE -> "early";
            case EXCUSED_ABSENCE -> "absent";
        };
    }

    private Absence createAbsenceFromStatus(Integer classId, Integer studentId, String status, Date date) {
        try {
            Class classEntity = classService.findById(classId);
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new RuntimeException("Student not found"));

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

            // Datum mit aktueller Unterrichtszeit kombinieren
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            Calendar timeCal = Calendar.getInstance();
            timeCal.setTime(classEntity.getStartTime());

            cal.set(Calendar.HOUR_OF_DAY, timeCal.get(Calendar.HOUR_OF_DAY));
            cal.set(Calendar.MINUTE, timeCal.get(Calendar.MINUTE));

            return new Absence(student, classEntity.getTeacher(), classEntity,
                    cal.getTime(), absenceType, reason);

        } catch (Exception e) {
            System.err.println("Error creating absence: " + e.getMessage());
            return null;
        }
    }

    private void deleteExistingAbsences(Date date) {
        // Bestehende Absenzen für diesen Tag löschen
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

        List<Absence> existingAbsences = absenceService.findByDateRange(startOfDay, endOfDay);
        for (Absence absence : existingAbsences) {
            // Nur Absenzen löschen, die durch Kalender-Erfassung erstellt wurden
            if (absence.getReason() != null && absence.getReason().contains("Kalender-Erfassung")) {
                absenceService.delete(absence.getId());
            }
        }
    }

    // Request DTO
    public static class AttendanceRequest {
        private Date date;
        private Map<String, String> attendanceData;

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public Map<String, String> getAttendanceData() {
            return attendanceData;
        }

        public void setAttendanceData(Map<String, String> attendanceData) {
            this.attendanceData = attendanceData;
        }
    }
}