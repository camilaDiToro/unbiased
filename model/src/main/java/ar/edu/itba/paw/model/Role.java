package ar.edu.itba.paw.model;

public enum Role {

    JOURNALIST("ROLE_JOURNALIST"),
    MODERATOR("ROLE_MODERATOR"),
    ADMIN("ROLE_ADMIN");

    private final String role;

    Role(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
