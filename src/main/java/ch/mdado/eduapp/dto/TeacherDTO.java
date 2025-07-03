package ch.mdado.eduapp.dto;

import ch.mdado.eduapp.models.Teacher;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

public class TeacherDTO implements Serializable {
    private static final long serialVersionUID = -157157293053537540L;

    @NotEmpty
    @Email
    private String email;

    @NotNull
    private Boolean hasSpecialNeeds;
    private String specialNeeds;

    @NotNull
    private String telephones;

    @NotEmpty
    private String password;

    public TeacherDTO() {
    }

    public TeacherDTO(Teacher teacher) {
        this.email = teacher.getEmail();
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
