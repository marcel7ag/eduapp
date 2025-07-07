package ch.mdado.eduapp.config;

import ch.mdado.eduapp.models.*;
import ch.mdado.eduapp.models.Class;
import ch.mdado.eduapp.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
@Transactional
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
        createTestClasses();
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

    private void createTestClasses() {
        try {
            // Frisch aus der Datenbank laden
            List<Teacher> teachers = teacherRepository.findAll();
            List<Student> students = studentRepository.findAll();

            if (teachers.isEmpty()) {
                System.err.println("No teachers found for creating classes.");
                return;
            }

            if (students.isEmpty()) {
                System.err.println("No students found for creating classes.");
                return;
            }

            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            Calendar cal = Calendar.getInstance();

            // Semester-Daten
            cal.set(2024, Calendar.AUGUST, 1);
            Date semesterStart = cal.getTime();
            cal.set(2025, Calendar.JANUARY, 31);
            Date semesterEnd = cal.getTime();

            // Klasse 1: Mathematik 9A
            Class math9A = new Class();
            math9A.setClassName("9A");
            math9A.setSubjectName("Mathematik");
            math9A.setTeacher(teachers.get(0));
            math9A.setDayOfWeek(1); // Montag
            math9A.setStartTime(timeFormat.parse("08:00"));
            math9A.setEndTime(timeFormat.parse("08:45"));
            math9A.setRoomNumber("M101");
            math9A.setMaxStudents(25);
            math9A.setSemesterStart(semesterStart);
            math9A.setSemesterEnd(semesterEnd);

            // Klasse 2: Deutsch 9A
            Class deutsch9A = new Class();
            deutsch9A.setClassName("9A");
            deutsch9A.setSubjectName("Deutsch");
            deutsch9A.setTeacher(teachers.get(1));
            deutsch9A.setDayOfWeek(2); // Dienstag
            deutsch9A.setStartTime(timeFormat.parse("09:00"));
            deutsch9A.setEndTime(timeFormat.parse("09:45"));
            deutsch9A.setRoomNumber("D201");
            deutsch9A.setMaxStudents(25);
            deutsch9A.setSemesterStart(semesterStart);
            deutsch9A.setSemesterEnd(semesterEnd);

            // Klasse 3: Englisch 9A
            Class englisch9A = new Class();
            englisch9A.setClassName("9A");
            englisch9A.setSubjectName("Englisch");
            englisch9A.setTeacher(teachers.get(2));
            englisch9A.setDayOfWeek(3); // Mittwoch
            englisch9A.setStartTime(timeFormat.parse("10:00"));
            englisch9A.setEndTime(timeFormat.parse("10:45"));
            englisch9A.setRoomNumber("E301");
            englisch9A.setMaxStudents(25);
            englisch9A.setSemesterStart(semesterStart);
            englisch9A.setSemesterEnd(semesterEnd);

            // Klasse 4: Geschichte 9A
            Class geschichte9A = new Class();
            geschichte9A.setClassName("9A");
            geschichte9A.setSubjectName("Geschichte");
            geschichte9A.setTeacher(teachers.get(3));
            geschichte9A.setDayOfWeek(4); // Donnerstag
            geschichte9A.setStartTime(timeFormat.parse("11:00"));
            geschichte9A.setEndTime(timeFormat.parse("11:45"));
            geschichte9A.setRoomNumber("G401");
            geschichte9A.setMaxStudents(25);
            geschichte9A.setSemesterStart(semesterStart);
            geschichte9A.setSemesterEnd(semesterEnd);

            // Klasse 5: Biologie 9B
            Class biologie9B = new Class();
            biologie9B.setClassName("9B");
            biologie9B.setSubjectName("Biologie");
            biologie9B.setTeacher(teachers.get(0));
            biologie9B.setDayOfWeek(5); // Freitag
            biologie9B.setStartTime(timeFormat.parse("13:00"));
            biologie9B.setEndTime(timeFormat.parse("13:45"));
            biologie9B.setRoomNumber("B501");
            biologie9B.setMaxStudents(20);
            biologie9B.setSemesterStart(semesterStart);
            biologie9B.setSemesterEnd(semesterEnd);

            // Klasse 6: Sport 9AB
            Class sport9AB = new Class();
            sport9AB.setClassName("9AB");
            sport9AB.setSubjectName("Sport");
            sport9AB.setTeacher(teachers.get(1));
            sport9AB.setDayOfWeek(5); // Freitag
            sport9AB.setStartTime(timeFormat.parse("14:00"));
            sport9AB.setEndTime(timeFormat.parse("15:30"));
            sport9AB.setRoomNumber("Turnhalle");
            sport9AB.setMaxStudents(30);
            sport9AB.setSemesterStart(semesterStart);
            sport9AB.setSemesterEnd(semesterEnd);

            // WICHTIG: Erst die Klassen ohne Schüler speichern
            List<Class> allClasses = Arrays.asList(math9A, deutsch9A, englisch9A, geschichte9A, biologie9B, sport9AB);
            classRepository.saveAll(allClasses);

            System.out.println("Classes saved, now assigning students...");

            // Jetzt die Schüler-Zuordnungen
            if (students.size() >= 6) {
                // Erste 3 Schüler zu 9A-Klassen
                List<Student> class9AStudents = students.subList(0, 3);
                for (Student student : class9AStudents) {
                    math9A.addStudent(student);
                    deutsch9A.addStudent(student);
                    englisch9A.addStudent(student);
                    geschichte9A.addStudent(student);
                    System.out.println("Added student " + student.getName() + " to 9A classes");
                }

                // Letzte 3 Schüler zu 9B-Klassen
                List<Student> class9BStudents = students.subList(3, 6);
                for (Student student : class9BStudents) {
                    biologie9B.addStudent(student);
                    System.out.println("Added student " + student.getName() + " to 9B classes");
                }

                // Alle Schüler zu Sport
                for (Student student : students) {
                    sport9AB.addStudent(student);
                    System.out.println("Added student " + student.getName() + " to Sport");
                }

                // Schüler-Zuordnungen speichern
                studentRepository.saveAll(students);

                // Klassen mit Schüler-Zuordnungen erneut speichern
                classRepository.saveAll(allClasses);

                System.out.println("Student assignments completed and saved.");
            }

            // Verification
            for (Class c : allClasses) {
                Class reloaded = classRepository.findById(c.getId()).orElse(null);
                if (reloaded != null) {
                    System.out.println("Class " + c.getClassName() + " - " + c.getSubjectName() +
                            " has " + reloaded.getStudents().size() + " students");
                }
            }

            System.out.println("Created 6 test classes with student assignments.");
        } catch (Exception e) {
            System.err.println("Error creating test classes: " + e.getMessage());
            e.printStackTrace();
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