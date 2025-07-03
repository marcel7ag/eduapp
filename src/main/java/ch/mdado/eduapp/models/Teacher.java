package ch.mdado.eduapp.models;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;

import java.util.Date;
import java.util.List;

@Entity
public class Teacher extends User{
    private static final long serialVersionUID = -5998799818393562142L;

    private String registration;

    @OneToMany(mappedBy = "teacher")
    private List<Class> classes;

    public Teacher() {
    }

    public Teacher(Integer id, String name, String email, Sex sex, Boolean hasSpecialNeeds, String specialNeeds, Date birthdate, String password, String registration) {
        super(id, name, email, sex, birthdate, password);
        super.addRole(Role.TEACHER);
    }

    public List<Class> getClasses() {
        return classes;
    }

    public void setClasses(List<Class> classes) {
        this.classes = classes;
    }
}
