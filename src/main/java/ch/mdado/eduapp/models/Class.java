package ch.mdado.eduapp.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "classes")
public class Class implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "class_name", nullable = false)
    private String className;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToMany(mappedBy = "classes", fetch = FetchType.LAZY)
    private Set<Student> students = new HashSet<>();

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "start_time", nullable = false)
    private Date startTime;

    @JsonFormat(pattern = "HH:mm")
    @Column(name = "end_time", nullable = false)
    private Date endTime;

    @Column(name = "day_of_week", nullable = false)
    private Integer dayOfWeek; // 1 = Montag, 2 = Dienstag, etc.

    @Column(name = "room_number")
    private String roomNumber;

    @Column(name = "max_students")
    private Integer maxStudents = 30;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive = true;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "semester_start")
    private Date semesterStart;

    @JsonFormat(pattern = "dd/MM/yyyy")
    @Column(name = "semester_end")
    private Date semesterEnd;

    @OneToMany(mappedBy = "classEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Absence> absences = new HashSet<>();

    // Konstruktoren
    public Class() {
    }

    public Class(String className, String subjectName, Teacher teacher, Date startTime, Date endTime, Integer dayOfWeek) {
        this.className = className;
        this.subjectName = subjectName;
        this.teacher = teacher;
        this.startTime = startTime;
        this.endTime = endTime;
        this.dayOfWeek = dayOfWeek;
    }

    // Hilfsmethoden
    public void addStudent(Student student) {
        this.students.add(student);
        student.getClasses().add(this);
    }

    public void removeStudent(Student student) {
        this.students.remove(student);
        student.getClasses().remove(this);
    }

    public String getDayOfWeekName() {
        return switch (dayOfWeek) {
            case 1 -> "Montag";
            case 2 -> "Dienstag";
            case 3 -> "Mittwoch";
            case 4 -> "Donnerstag";
            case 5 -> "Freitag";
            case 6 -> "Samstag";
            case 7 -> "Sonntag";
            default -> "Unbekannt";
        };
    }

    // Getters und Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Set<Student> getStudents() {
        return students;
    }

    public void setStudents(Set<Student> students) {
        this.students = students;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Date getSemesterStart() {
        return semesterStart;
    }

    public void setSemesterStart(Date semesterStart) {
        this.semesterStart = semesterStart;
    }

    public Date getSemesterEnd() {
        return semesterEnd;
    }

    public void setSemesterEnd(Date semesterEnd) {
        this.semesterEnd = semesterEnd;
    }

    public Set<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(Set<Absence> absences) {
        this.absences = absences;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Class)) return false;
        Class aClass = (Class) o;
        return Objects.equals(id, aClass.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return className + " - " + subjectName + " (" + getDayOfWeekName() + ")";
    }
}