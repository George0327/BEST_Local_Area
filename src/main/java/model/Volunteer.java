package model;

import java.time.LocalDate;

public class Volunteer {
    private int id;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private LocalDate birthday;
    private boolean isActive;

    public Volunteer(int id, String firstname, String lastname, String email, String phone, LocalDate birthday, boolean isActive) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
        this.isActive = isActive;
    }

    public Volunteer(String firstName, String lastName, String email, String phone, LocalDate birthday) {
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    public Volunteer(int id, String firstName, String lastName, String email, String phone, LocalDate birthday) {
        this.id = id;
        this.firstname = firstName;
        this.lastname = lastName;
        this.email = email;
        this.phone = phone;
        this.birthday = birthday;
    }

    public Volunteer(int id, String firstName, String lastName) {
        this.id = id;
        this.firstname = firstName;
        this.lastname = lastName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDate getBirthday() {
        return birthday;
    }

    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
