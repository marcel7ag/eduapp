package ch.mdado.eduapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Student extends User {
    private static final long serialVersionUID = 7941636334360165045L;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "student_class",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "class_id")
    )
    @JsonIgnoreProperties({"students", "teacher", "absences"})
    private Set<Class> classes = new HashSet<>();

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"student", "teacher", "classEntity"})
    private Set<Absence> absences = new HashSet<>();

    public Student() {
    }

    public Student(Integer id, String name, String email, Sex sex, Date birthdate, String password) {
        super(id, name, email, sex, birthdate, password);
        super.addRole(Role.STUDENT);
    }

    public Set<Class> getClasses() {
        return classes;
    }

    public void setClasses(Set<Class> classes) {
        this.classes = classes;
    }

    public Set<Absence> getAbsences() {
        return absences;
    }

    public void setAbsences(Set<Absence> absences) {
        this.absences = absences;
    }
}