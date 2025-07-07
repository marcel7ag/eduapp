package ch.mdado.eduapp.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Teacher extends User{
    private static final long serialVersionUID = -5998799818393562142L;

    private String registration;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"teacher", "students", "absences"})
    private Set<Class> classes = new HashSet<>();

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnoreProperties({"teacher", "student", "classEntity"})
    private Set<Absence> absences = new HashSet<>();

    public Teacher() {
    }

    public Teacher(Integer id, String name, String email, Sex sex, Boolean hasSpecialNeeds, String specialNeeds, Date birthdate, String password, String registration) {
        super(id, name, email, sex, birthdate, password);
        this.registration = registration;
        super.addRole(Role.TEACHER);
    }

    public String getRegistration() {
        return registration;
    }

    public void setRegistration(String registration) {
        this.registration = registration;
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