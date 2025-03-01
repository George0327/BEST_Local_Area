package model;

public class CoreTeam {

    private int id;
    private String teamName;
    private String description;
    private int leaderId;

    private String mainOrganiser;

    private String project;

    public CoreTeam (int id, String teamName, String description, int leaderId){
        this.id = id;
        this.teamName = teamName;
        this.description = description;
        this.leaderId = leaderId;
    }

    public CoreTeam (int id, String teamName, int leaderId, String mainOrganiser, String project){
        this.id = id;
        this.teamName = teamName;
        this.leaderId = leaderId;
        this.mainOrganiser = mainOrganiser;
        this.project = project;
    }

    public CoreTeam(int id, String teamName){
        this.id = id;
        this.teamName = teamName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLeaderId() {
        return leaderId;
    }

    public void setLeaderId(int leaderId) {
        this.leaderId = leaderId;
    }

    public String getMainOrganiser() {
        return mainOrganiser;
    }

    public void setMainOrganiser(String mainOrganiser) {
        this.mainOrganiser = mainOrganiser;
    }

    public String getProject() {
        return project;
    }

    public void setProject(String project) {
        this.project = project;
    }
}
