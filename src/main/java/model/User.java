package model;

public class User {
    private int id;
    private String username;
    private String password;
    private int volunteerID;
    private String role;
    private String volunteerName;

    // Constructor
    public User(int id, String username, String password, int volunteerID, String role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.volunteerID = volunteerID;
        this.role = role;
    }

    public User(int id, String username, String password, String volunteerName, String role){
        this.id = id;
        this.username = username;
        this.password = password;
        this.volunteerName = volunteerName;
        this.role = role;
    }

    public User(int id, String username, String password, String role){
        this.id = id;
        this.username = username;
        this.password = password;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getVolunteerID() {
        return volunteerID;
    }

    public void setVolunteerID(int volunteerID) {
        this.volunteerID = volunteerID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getVolunteerName() {
        return volunteerName;
    }

    public void setVolunteerName(String volunteerName) {
        this.volunteerName = volunteerName;
    }
}