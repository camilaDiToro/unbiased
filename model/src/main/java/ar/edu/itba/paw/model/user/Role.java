package ar.edu.itba.paw.model.user;

public enum Role {
    JOURNALIST("ROLE_JOURNALIST"),
    ADMIN("ROLE_ADMIN");

    private final String role;
    Role(String role){
        this.role = role;
    }
    public String getRole() {
        return role;
    }

    public static Role getRole(String s) {
        for (Role role : values()) {
            if (role.getRole().equals(s))
                return role;
        }
        throw new IllegalArgumentException();
    }
}
