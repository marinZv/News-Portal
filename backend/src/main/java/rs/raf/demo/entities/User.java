package rs.raf.demo.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Path;

public class User {

    private Integer id;

    @NotNull(message = "first_name required")
    @NotEmpty(message = "first_name required")
    @JsonProperty("first_name")
    private String firstName;

    @NotNull
    @NotEmpty
    @JsonProperty("last_name")
    private String lastName;
    private int role;

    @NotNull(message = "password required")
    @NotEmpty(message = "password required")
    private String password;

    @NotNull(message = "email required")
    @NotEmpty(message = "email required")
    private String email;


    private int status;


    public User(){

    }

    public User(Integer id, String firstName, String lastName, String email, String password){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
    }

    public User(Integer id, String firstName, String lastName, String email, String password, int role, int status){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getRole() {
        return role;
    }

    public void setRole(int role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
