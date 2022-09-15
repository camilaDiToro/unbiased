package ar.edu.itba.paw.model;

public enum Role {

    REGISTERED("REGISTERED"),
    UNREGISTERED("UNREGISTERED"),
    UNABLE("UNABLE");

    private final String role;

    Role(String role){
        this.role = role;
    }

    public String getRole() {
        return role;
    }
}
