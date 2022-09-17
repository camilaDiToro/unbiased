package ar.edu.itba.paw.model;

public enum Role {

    JOURNALIST("JOURNALIST"),
    MODERATOR("MODERATOR");

    private final String role;

    Role(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
