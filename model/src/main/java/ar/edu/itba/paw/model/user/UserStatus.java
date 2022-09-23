package ar.edu.itba.paw.model.user;

public enum UserStatus {

    REGISTERED("REGISTERED"),
    UNREGISTERED("UNREGISTERED"),
    UNABLE("UNABLE");

    private final String status;

    UserStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public static UserStatus getByDesc(String description){
        for (UserStatus c : UserStatus.values()) {
            if (c.getStatus().equals(description) ) {
                return c;
            }
        }
        return null;
    }
}
