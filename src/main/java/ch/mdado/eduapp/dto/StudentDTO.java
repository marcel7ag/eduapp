package ch.mdado.eduapp.dto;

import ch.mdado.eduapp.models.Student;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class StudentDTO implements Serializable {
    private static final long serialVersionUID = -8650116971354996096L;

    private Integer id;

    @NotEmpty
    private String name;

    @NotEmpty
    @Email
    private String email;

    @NotNull
    private String password;

    @NotNull
    private String telephone;

    public StudentDTO() {
    }

    public StudentDTO(Student student) {
        this.name = student.getName();
        this.email = student.getEmail();
        this.password = student.getPassword();
        this.telephone = student.getTelephone();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
}
