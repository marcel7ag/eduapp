package ch.mdado.eduapp.models;

import jakarta.persistence.Entity;

import java.util.Date;
// TODO
// so liste vom student selbst, welche fächer er zugewiesen ist etc.
@Entity
public class Student extends User {
    private static final long serialVersionUID = 7941636334360165045L;

    public Student() {
    }

    public Student(Integer id, String name, String cpf, String rg, String email, Sex sex, String specialNeeds, Date birthdate, String password) {
        super(id, name, cpf, rg, email, sex, birthdate, password);
        super.addRole(Role.STUDENT);
    }

}