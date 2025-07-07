package ch.mdado.eduapp.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "absences")
public class Absence implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne
    @JoinColumn(name = "teacher_id", nullable = false)
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "class_id")
    private Class classEntity;

    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @Column(name = "absence_date", nullable = false)
    private Date absenceDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "absence_type", nullable = false)
    private AbsenceType absenceType;

    @Column(name = "reason", length = 500)
    private String reason;

    @Column(name = "is_excused", nullable = false)
    private Boolean isExcused = false;

    @Column(name = "excuse_document")
    private String excuseDocument; // Pfad zu hochgeladenen Dokumenten

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @Column(name = "created_at", nullable = false)
    private Date createdAt;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @Column(name = "updated_at")
    private Date updatedAt;

    // Konstruktoren
    public Absence() {
        this.createdAt = new Date();
    }

    public Absence(Student student, Teacher teacher, String subjectName, Date absenceDate,
                   AbsenceType absenceType, String reason) {
        this();
        this.student = student;
        this.teacher = teacher;
        this.subjectName = subjectName;
        this.absenceDate = absenceDate;
        this.absenceType = absenceType;
        this.reason = reason;
    }

    public Absence(Student student, Teacher teacher, Class classEntity, Date absenceDate,
                   AbsenceType absenceType, String reason) {
        this();
        this.student = student;
        this.teacher = teacher;
        this.classEntity = classEntity;
        this.subjectName = classEntity != null ? classEntity.getSubjectName() : null;
        this.absenceDate = absenceDate;
        this.absenceType = absenceType;
        this.reason = reason;
    }

    // Getters und Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Class getClassEntity() {
        return classEntity;
    }

    public void setClassEntity(Class classEntity) {
        this.classEntity = classEntity;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public Date getAbsenceDate() {
        return absenceDate;
    }

    public void setAbsenceDate(Date absenceDate) {
        this.absenceDate = absenceDate;
    }

    public AbsenceType getAbsenceType() {
        return absenceType;
    }

    public void setAbsenceType(AbsenceType absenceType) {
        this.absenceType = absenceType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Boolean getIsExcused() {
        return isExcused;
    }

    public void setIsExcused(Boolean isExcused) {
        this.isExcused = isExcused;
    }

    public String getExcuseDocument() {
        return excuseDocument;
    }

    public void setExcuseDocument(String excuseDocument) {
        this.excuseDocument = excuseDocument;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = new Date();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Absence)) return false;
        Absence absence = (Absence) o;
        return Objects.equals(id, absence.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}