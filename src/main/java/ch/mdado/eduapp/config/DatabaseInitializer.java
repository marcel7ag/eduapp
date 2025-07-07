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
            // Klasse 9A Schüler (15 Schüler)
            Student[] students9A = {
                    new Student(null, "Max Mustermann", "max.mustermann@example.com", Sex.MASCULINE, sdf.parse("15/05/2008"), "password123"),
                    new Student(null, "Anna Schmidt", "anna.schmidt@example.com", Sex.FEMININE, sdf.parse("22/08/2007"), "password123"),
                    new Student(null, "Tim Weber", "tim.weber@example.com", Sex.MASCULINE, sdf.parse("10/12/2008"), "password123"),
                    new Student(null, "Lisa Müller", "lisa.mueller@example.com", Sex.FEMININE, sdf.parse("03/03/2007"), "password123"),
                    new Student(null, "Jonas Keller", "jonas.keller@example.com", Sex.MASCULINE, sdf.parse("28/11/2008"), "password123"),
                    new Student(null, "Sarah Fischer", "sarah.fischer@example.com", Sex.FEMININE, sdf.parse("17/06/2007"), "password123"),
                    new Student(null, "David Richter", "david.richter@example.com", Sex.MASCULINE, sdf.parse("14/09/2008"), "password123"),
                    new Student(null, "Emma Wagner", "emma.wagner@example.com", Sex.FEMININE, sdf.parse("25/04/2007"), "password123"),
                    new Student(null, "Lukas Bauer", "lukas.bauer@example.com", Sex.MASCULINE, sdf.parse("07/01/2008"), "password123"),
                    new Student(null, "Mia Hoffmann", "mia.hoffmann@example.com", Sex.FEMININE, sdf.parse("19/10/2007"), "password123"),
                    new Student(null, "Felix Schneider", "felix.schneider@example.com", Sex.MASCULINE, sdf.parse("02/07/2008"), "password123"),
                    new Student(null, "Lea Zimmermann", "lea.zimmermann@example.com", Sex.FEMININE, sdf.parse("13/12/2007"), "password123"),
                    new Student(null, "Noah Klein", "noah.klein@example.com", Sex.MASCULINE, sdf.parse("08/05/2008"), "password123"),
                    new Student(null, "Sophia Wolf", "sophia.wolf@example.com", Sex.FEMININE, sdf.parse("26/02/2007"), "password123"),
                    new Student(null, "Ben Schröder", "ben.schroeder@example.com", Sex.MASCULINE, sdf.parse("11/09/2008"), "password123")
            };

            // Klasse 9B Schüler (14 Schüler)
            Student[] students9B = {
                    new Student(null, "Lara Neumann", "lara.neumann@example.com", Sex.FEMININE, sdf.parse("16/03/2008"), "password123"),
                    new Student(null, "Paul Schwarz", "paul.schwarz@example.com", Sex.MASCULINE, sdf.parse("21/06/2007"), "password123"),
                    new Student(null, "Hannah Braun", "hannah.braun@example.com", Sex.FEMININE, sdf.parse("04/11/2008"), "password123"),
                    new Student(null, "Tom Krüger", "tom.krueger@example.com", Sex.MASCULINE, sdf.parse("29/08/2007"), "password123"),
                    new Student(null, "Zoe Hartmann", "zoe.hartmann@example.com", Sex.FEMININE, sdf.parse("12/01/2008"), "password123"),
                    new Student(null, "Jan Lange", "jan.lange@example.com", Sex.MASCULINE, sdf.parse("18/04/2008"), "password123"),
                    new Student(null, "Nora Schmitt", "nora.schmitt@example.com", Sex.FEMININE, sdf.parse("03/10/2007"), "password123"),
                    new Student(null, "Leon Werner", "leon.werner@example.com", Sex.MASCULINE, sdf.parse("27/12/2008"), "password123"),
                    new Student(null, "Clara König", "clara.koenig@example.com", Sex.FEMININE, sdf.parse("14/07/2007"), "password123"),
                    new Student(null, "Finn Krause", "finn.krause@example.com", Sex.MASCULINE, sdf.parse("09/05/2008"), "password123"),
                    new Student(null, "Maya Berger", "maya.berger@example.com", Sex.FEMININE, sdf.parse("23/09/2007"), "password123"),
                    new Student(null, "Nick Franke", "nick.franke@example.com", Sex.MASCULINE, sdf.parse("06/02/2008"), "password123"),
                    new Student(null, "Ava Peters", "ava.peters@example.com", Sex.FEMININE, sdf.parse("20/11/2007"), "password123"),
                    new Student(null, "Elias Graf", "elias.graf@example.com", Sex.MASCULINE, sdf.parse("15/06/2008"), "password123")
            };

            // Adressen und Telefonnummern für Klasse 9A setzen
            String[] addresses9A = {
                    "Musterstraße 1, 3000 Bern", "Schulstraße 15, 3000 Bern", "Lernweg 8, 3000 Bern",
                    "Bildungsstraße 22, 3000 Bern", "Wissensplatz 5, 3000 Bern", "Lehrstraße 12, 3000 Bern",
                    "Kirchenfeld 7, 3000 Bern", "Bollwerk 3, 3000 Bern", "Kramgasse 19, 3000 Bern",
                    "Bundesplatz 2, 3000 Bern", "Elfenau 11, 3000 Bern", "Länggasse 25, 3000 Bern",
                    "Wankdorf 4, 3000 Bern", "Holligen 17, 3000 Bern", "Bümpliz 9, 3000 Bern"
            };

            String[] addresses9B = {
                    "Neufeld 8, 3000 Bern", "Wittigkofen 14, 3000 Bern", "Bethlehem 6, 3000 Bern",
                    "Köniz 21, 3000 Bern", "Ostermundigen 13, 3000 Bern", "Ittigen 5, 3000 Bern",
                    "Muri 18, 3000 Bern", "Worb 12, 3000 Bern", "Zollikofen 7, 3000 Bern",
                    "Bolligen 16, 3000 Bern", "Kehrsatz 3, 3000 Bern", "Oberbalm 20, 3000 Bern",
                    "Laupen 10, 3000 Bern", "Münsingen 15, 3000 Bern"
            };

            // Schweizer Telefonnummern setzen
            String[] swissNumbers9A = {
                    "078 123 45 67", "079 234 56 78", "076 345 67 89", "077 456 78 90", "078 567 89 01",
                    "079 678 90 12", "076 789 01 23", "077 890 12 34", "078 901 23 45", "079 012 34 56",
                    "076 123 45 67", "077 234 56 78", "078 345 67 89", "079 456 78 90", "076 567 89 01"
            };

            String[] swissNumbers9B = {
                    "078 678 90 12", "079 789 01 23", "076 890 12 34", "077 901 23 45", "078 012 34 56",
                    "079 123 45 67", "076 234 56 78", "077 345 67 89", "078 456 78 90", "079 567 89 01",
                    "076 678 90 12", "077 789 01 23", "078 890 12 34", "079 901 23 45"
            };

            for (int i = 0; i < students9A.length; i++) {
                students9A[i].setTelephone(swissNumbers9A[i]);
                students9A[i].setAddress(addresses9A[i]);
            }

            for (int i = 0; i < students9B.length; i++) {
                students9B[i].setTelephone(swissNumbers9B[i]);
                students9B[i].setAddress(addresses9B[i]);
            }

            // Alle Schüler speichern
            studentRepository.saveAll(Arrays.asList(students9A));
            studentRepository.saveAll(Arrays.asList(students9B));

            System.out.println("Created " + (students9A.length + students9B.length) + " test students.");
            System.out.println("- Klasse 9A: " + students9A.length + " Schüler");
            System.out.println("- Klasse 9B: " + students9B.length + " Schüler");

        } catch (Exception e) {
            System.err.println("Error creating test students: " + e.getMessage());
            e.printStackTrace();
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

            // Klasse 7: Italienisch 9A
            Class italian9A = new Class();
            italian9A.setClassName("9A");
            italian9A.setSubjectName("Italienisch");
            italian9A.setTeacher(teachers.get(1));
            italian9A.setDayOfWeek(1); // Montag
            italian9A.setStartTime(timeFormat.parse("10:00"));
            italian9A.setEndTime(timeFormat.parse("12:00"));
            italian9A.setRoomNumber("M132");
            italian9A.setMaxStudents(30);
            italian9A.setSemesterStart(semesterStart);
            italian9A.setSemesterEnd(semesterEnd);

            // Klasse 8: Italienisch 9B
            Class italian9B = new Class();
            italian9B.setClassName("9B");
            italian9B.setSubjectName("Italienisch");
            italian9B.setTeacher(teachers.get(1));
            italian9B.setDayOfWeek(1); // Montag
            italian9B.setStartTime(timeFormat.parse("10:00"));
            italian9B.setEndTime(timeFormat.parse("12:00"));
            italian9B.setRoomNumber("M133");
            italian9B.setMaxStudents(30);
            italian9B.setSemesterStart(semesterStart);
            italian9B.setSemesterEnd(semesterEnd);

            // Klasse 9: Russisch 9AB
            Class russian9AB = new Class();
            russian9AB.setClassName("9AB");
            russian9AB.setSubjectName("Russisch");
            russian9AB.setTeacher(teachers.get(1));
            russian9AB.setDayOfWeek(1); // Montag
            russian9AB.setStartTime(timeFormat.parse("14:00"));
            russian9AB.setEndTime(timeFormat.parse("16:00"));
            russian9AB.setRoomNumber("M235");
            russian9AB.setMaxStudents(30);
            russian9AB.setSemesterStart(semesterStart);
            russian9AB.setSemesterEnd(semesterEnd);

            // Klasse 10: Russisch 9AB
            Class russian9B = new Class();
            russian9B.setClassName("9B");
            russian9B.setSubjectName("Russisch");
            russian9B.setTeacher(teachers.get(1));
            russian9B.setDayOfWeek(2); // Dienstag
            russian9B.setStartTime(timeFormat.parse("10:00"));
            russian9B.setEndTime(timeFormat.parse("13:00"));
            russian9B.setRoomNumber("M235");
            russian9B.setMaxStudents(30);
            russian9B.setSemesterStart(semesterStart);
            russian9B.setSemesterEnd(semesterEnd);

            // WICHTIG: Erst die Klassen ohne Schüler speichern
            List<Class> allClasses = Arrays.asList(math9A, deutsch9A, englisch9A, geschichte9A, biologie9B, sport9AB);
            classRepository.saveAll(allClasses);

            System.out.println("Classes saved, now assigning students...");

            // Schüler-Zuordnungen basierend auf Reihenfolge
            // Erste 15 Schüler zu 9A-Klassen
            List<Student> class9AStudents = students.subList(0, Math.min(15, students.size()));
            for (Student student : class9AStudents) {
                math9A.addStudent(student);
                deutsch9A.addStudent(student);
                englisch9A.addStudent(student);
                geschichte9A.addStudent(student);
                System.out.println("Added student " + student.getName() + " to 9A classes");
            }

            // Nächste 14 Schüler zu 9B-Klassen
            int startIndex9B = Math.min(15, students.size());
            int endIndex9B = Math.min(29, students.size());
            if (startIndex9B < students.size()) {
                List<Student> class9BStudents = students.subList(startIndex9B, endIndex9B);
                for (Student student : class9BStudents) {
                    biologie9B.addStudent(student);
                    System.out.println("Added student " + student.getName() + " to 9B classes");
                }
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
            String[] subjects = {"Mathematik", "Deutsch", "Englisch", "Geschichte", "Biologie", "Sport"};

            // Absenzen der letzten 4 Wochen erstellen (mehr Daten für bessere Tests)
            for (int i = 0; i < 60; i++) {
                Student student = students.get(i % students.size());
                Teacher teacher = teachers.get(i % teachers.size());
                String subject = subjects[i % subjects.length];

                // Zufälliges Datum in den letzten 28 Tagen
                cal.setTime(new Date());
                cal.add(Calendar.DAY_OF_MONTH, -(int)(Math.random() * 28));
                cal.set(Calendar.HOUR_OF_DAY, 8 + (int)(Math.random() * 8)); // 8-16 Uhr
                cal.set(Calendar.MINUTE, (int)(Math.random() * 60));
                Date absenceDate = cal.getTime();

                // Zufälliger Absenztyp
                AbsenceType[] types = AbsenceType.values();
                AbsenceType absenceType = types[(int)(Math.random() * types.length)];

                // Grund basierend auf Typ
                String reason = getRandomReason(absenceType);

                Absence absence = new Absence(student, teacher, subject, absenceDate, absenceType, reason);

                // 75% der Absenzen sind entschuldigt
                if (Math.random() < 0.75) {
                    absence.setIsExcused(true);
                    absence.setExcuseDocument("Entschuldigung_" + student.getName().replace(" ", "_") + "_" + i + ".pdf");
                }

                absenceRepository.save(absence);
            }

            // Einige Absenzen für heute erstellen
            cal.setTime(new Date());
            for (int i = 0; i < 8; i++) {
                Student student = students.get(i % students.size());
                Teacher teacher = teachers.get(i % teachers.size());
                String subject = subjects[i % subjects.length];

                cal.set(Calendar.HOUR_OF_DAY, 8 + i);
                cal.set(Calendar.MINUTE, 0);
                Date todayAbsenceDate = cal.getTime();

                AbsenceType absenceType = i < 3 ? AbsenceType.LATE : AbsenceType.ABSENT;
                String reason = getRandomReason(absenceType);

                Absence todayAbsence = new Absence(student, teacher, subject, todayAbsenceDate, absenceType, reason);

                // Nur 40% der heutigen Absenzen sind entschuldigt
                if (Math.random() < 0.4) {
                    todayAbsence.setIsExcused(true);
                    todayAbsence.setExcuseDocument("Entschuldigung_heute_" + i + ".pdf");
                }

                absenceRepository.save(todayAbsence);
            }

            System.out.println("Created 68 test absences (60 historical + 8 today).");
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
                        "Familiäre Umstände",
                        "Bus verpasst",
                        "Zug hatte Verspätung"
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
                        "Zahnarzttermin",
                        "Fieber",
                        "Kopfschmerzen",
                        "Bauchschmerzen"
                };
            }
            case EARLY_LEAVE -> {
                yield new String[]{
                        "Arzttermin",
                        "Familiäre Notlage",
                        "Übelkeit",
                        "Wichtiger Termin",
                        "Unwohlsein",
                        "Zahnarzttermin",
                        "Therapeutentermin"
                };
            }
            case EXCUSED_ABSENCE -> {
                yield new String[]{
                        "Krankheit mit Attest",
                        "Familiäre Beerdigung",
                        "Behördengang",
                        "Medizinischer Notfall",
                        "Schüleraustausch",
                        "Arzttermin mit Attest",
                        "Familiärer Notfall"
                };
            }
        };

        return reasons[(int)(Math.random() * reasons.length)];
    }
}