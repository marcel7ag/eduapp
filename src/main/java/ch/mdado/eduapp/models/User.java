package ch.mdado.eduapp.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class User implements Serializable {

    private static final long serialVersionUID = -5562036392856704832L;
    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Integer id;
    private String name;
    private String email;
    private Character sex;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date birthdate;

    @JsonIgnore
    private String password;

    private String telephone;

    private String address;

    @ElementCollection(fetch=FetchType.EAGER)
    @CollectionTable(name="ROLES")
    private Set<Integer> roles = new HashSet<>();

    public User() {
    }

    public User(Integer id, String name, String email, Sex sex, Date birthdate, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.sex = (sex == null)? null : sex.getCode();
        this.birthdate = birthdate;
        this.password = password;
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

    public Sex getSex() {
        return Sex.toEnum(sex);
    }

    public void setSex(Sex sex) {
        this.sex =  (sex == null)? null : sex.getCode();
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Set<Role> getRoles() {
        return roles.stream().map(Role::toEnum).collect(Collectors.toSet());
    }

    public void addRole(Role role) {
        this.roles.add(role.getCode());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
