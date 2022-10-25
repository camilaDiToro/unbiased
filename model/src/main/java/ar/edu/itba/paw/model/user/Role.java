package ar.edu.itba.paw.model.user;

public enum Role {
    ROLE_JOURNALIST("ROLE_JOURNALIST"),
    ROLE_ADMIN("ROLE_ADMIN"),
    ROLE_OWNER("ROLE_OWNER");

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
