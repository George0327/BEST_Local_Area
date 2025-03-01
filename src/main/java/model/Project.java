package model;

public class Project {
    int id;

    String projectName;

    int ctId;

    String ctName;

    public Project(int id, String projectName, int ctId, String ctName){
        this.id =id;
        this.projectName = projectName;
        this.ctId = ctId;
        this.ctName = ctName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public int getCtId() {
        return ctId;
    }

    public void setCtId(int ctId) {
        this.ctId = ctId;
    }

    public String getCtName() {
        return ctName;
    }

    public void setCtName(String ctName) {
        this.ctName = ctName;
    }
}
