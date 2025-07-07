package ch.mdado.eduapp.config;

import ch.mdado.eduapp.models.*;
import ch.mdado.eduapp.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

@Component
public class DatabaseInitializer implements CommandLineRunner {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private AbsenceRepository absenceRepository;

    @Autowired
    private ClassRepository classRepository;

    @Override
    public void run(String... args) throws Exception {
        if (studentRepository.count() == 0) {
            initializeDatabase();
        }
    }

    private void initializeDatabase() {
        System.out.println("Initializing database with test data...");

        // Testdaten erstellen
        createTestStudents();
        createTestTeachers();
        createTestAbsences();

        System.out.println("Database initialization completed.");
    }

    private void createTestStudents() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            // Schüler erstellen
            Student student1 = new Student(null, "Max Mustermann", "max.mustermann@example.com",
                    Sex.MASCULINE, sdf.parse("15/05/2008"), "password123");
            student1.setTelephone("031-555-0101");
            student1.setAddress("Musterstraße 1, 3000 Bern");

            Student student2 = new Student(null, "Anna Schmidt", "anna.schmidt@example.com",
                    Sex.FEMININE, sdf.parse("22/08/2007"), "password123");
            student2.setTelephone("031-555-0102");
            student2.setAddress("Schulstraße 15, 3000 Bern");

            Student student3 = new Student(null, "Tim Weber", "tim.weber@example.com",
                    Sex.MASCULINE, sdf.parse("10/12/2008"), "password123");
            student3.setTelephone("031-555-0103");
            student3.setAddress("Lernweg 8, 3000 Bern");

            Student student4 = new Student(null, "Lisa Müller", "lisa.mueller@example.com",
                    Sex.FEMININE, sdf.parse("03/03/2007"), "password123");
            student4.setTelephone("031-555-0104");
            student4.setAddress("Bildungsstraße 22, 3000 Bern");

            Student student5 = new Student(null, "Jonas Keller", "jonas.keller@example.com",
                    Sex.MASCULINE, sdf.parse("28/11/2008"), "password123");
            student5.setTelephone("031-555-0105");
            student5.setAddress("Wissensplatz 5, 3000 Bern");

            Student student6 = new Student(null, "Sarah Fischer", "sarah.fischer@example.com",
                    Sex.FEMININE, sdf.parse("17/06/2007"), "password123");
            student6.setTelephone("031-555-0106");
            student6.setAddress("Lehrstraße 12, 3000 Bern");

            // Schüler speichern
            studentRepository.saveAll(Arrays.asList(student1, student2, student3, student4, student5, student6));

            System.out.println("Created 6 test students.");
        } catch (Exception e) {
            System.err.println("Error creating test students: " + e.getMessage());
        }
    }

    private void createTestTeachers() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        try {
            // Lehrer erstellen
            Teacher teacher1 = new Teacher(null, "Frau Schmidt", "schmidt@school.com",
                    Sex.FEMININE, false, null, sdf.parse("15/08/1985"), "password123", "T001");
            teacher1.setTelephone("031-555-0201");
            teacher1.setAddress("Lehrerstraße 10, 3000 Bern");

            Teacher teacher2 = new Teacher(null, "Herr Müller", "mueller@school.com",
                    Sex.MASCULINE, false, null, sdf.parse("22/03/1980"), "password123", "T002");
            teacher2.setTelephone("031-555-0202");
            teacher2.setAddress("Pädagogikweg 7, 3000 Bern");

            Teacher teacher3 = new Teacher(null, "Frau Weber", "weber@school.com",
                    Sex.FEMININE, false, null, sdf.parse("10/11/1978"), "password123", "T003");
            teacher3.setTelephone("031-555-0203");
            teacher3.setAddress("Bildungsallee 3, 3000 Bern");

            Teacher teacher4 = new Teacher(null, "Herr Fischer", "fischer@school.com",
                    Sex.MASCULINE, false, null, sdf.parse("05/07/1982"), "password123", "T004");
            teacher4.setTelephone("031-555-0204");
            teacher4.setAddress("Schulhausplatz 1, 3000 Bern");

            // Lehrer speichern
            teacherRepository.saveAll(Arrays.asList(teacher1, teacher2, teacher3, teacher4));

            System.out.println("Created 4 test teachers.");
        } catch (Exception e) {
            System.err.println("Error creating test teachers: " + e.getMessage());
        }
    }

    private void createTestAbsences() {
        try {
            // Alle Schüler und Lehrer laden
            var students = studentRepository.findAll();
            var teachers = teacherRepository.findAll();

            if (students.isEmpty() || teachers.isEmpty()) {
                System.err.println("No students or teachers found for creating absences.");
                return;
            }

            Calendar cal = Calendar.getInstance();

            // Verschiedene Fächer
            String[] subjects = {"Mathematik", "Deutsch", "Englisch", "Geschichte", "Biologie", "Chemie", "Physik", "Sport"};

            // Absenzen der letzten 2 Wochen erstellen
            for (int i = 0; i < 25; i++) {
                Student student = students.get(i % students.size());
                Teacher teacher = teachers.get(i % teachers.size());
                String subject = subjects[i % subjects.length];

                // Zufälliges Datum in den letzten 14 Tagen
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_MONTH, -(int)(Math.random() * 14));
                cal.set(Calendar.HOUR_OF_DAY, 8 + (int)(Math.random() * 8)); // 8-16 Uhr
                cal.set(Calendar.MINUTE, (int)(Math.random() * 60));
                Date absenceDate = cal.getTime();

                // Zufälliger Absenztyp
                AbsenceType[] types = AbsenceType.values();
                AbsenceType absenceType = types[(int)(Math.random() * types.length)];

                // Grund basierend auf Typ
                String reason = getRandomReason(absenceType);

                Absence absence = new Absence(student, teacher, subject, absenceDate, absenceType, reason);

                // 70% der Absenzen sind entschuldigt
                if (Math.random() < 0.7) {
                    absence.setIsExcused(true);
                    absence.setExcuseDocument("Entschuldigung_" + student.getName().replace(" ", "_") + "_" + i + ".pdf");
                }

                absenceRepository.save(absence);
            }

            // Einige Absenzen für heute erstellen
            cal.setTime(new Date());
            for (int i = 0; i < 5; i++) {
                Student student = students.get(i % students.size());
                Teacher teacher = teachers.get(i % teachers.size());
                String subject = subjects[i % subjects.length];

                cal.set(Calendar.HOUR_OF_DAY, 8 + i * 2);
                cal.set(Calendar.MINUTE, 0);
                Date todayAbsenceDate = cal.getTime();

                AbsenceType absenceType = i < 2 ? AbsenceType.LATE : AbsenceType.ABSENT;
                String reason = getRandomReason(absenceType);

                Absence todayAbsence = new Absence(student, teacher, subject, todayAbsenceDate, absenceType, reason);

                // Nur 30% der heutigen Absenzen sind entschuldigt
                if (Math.random() < 0.3) {
                    todayAbsence.setIsExcused(true);
                    todayAbsence.setExcuseDocument("Entschuldigung_heute_" + i + ".pdf");
                }

                absenceRepository.save(todayAbsence);
            }

            System.out.println("Created 30 test absences (25 historical + 5 today).");
        } catch (Exception e) {
            System.err.println("Error creating test absences: " + e.getMessage());
        }
    }

    private String getRandomReason(AbsenceType type) {
        String[] reasons = switch (type) {
            case LATE -> {
                yield new String[]{
                        "Verspätung durch öffentliche Verkehrsmittel",
                        "Stau auf dem Schulweg",
                        "Verschlafen",
                        "Arzttermin am Morgen",
                        "Familiäre Umstände"
                };
            }
            case ABSENT -> {
                yield new String[]{
                        "Krankheit",
                        "Arzttermin",
                        "Familiäre Verpflichtungen",
                        "Ohne Entschuldigung",
                        "Schwänzen",
                        "Grippe",
                        "Zahnarzttermin"
                };
            }
            case EARLY_LEAVE -> {
                yield new String[]{
                        "Arzttermin",
                        "Familiäre Notlage",
                        "Übelkeit",
                        "Wichtiger Termin",
                        "Unwohlsein"
                };
            }
            case EXCUSED_ABSENCE -> {
                yield new String[]{
                        "Krankheit mit Attest",
                        "Familiäre Beerdigung",
                        "Behördengang",
                        "Medizinischer Notfall",
                        "Schüleraustausch"
                };
            }
        };

        return reasons[(int)(Math.random() * reasons.length)];
    }
}