package ar.edu.itba.paw.model;

public enum UserStatus {

    REGISTERED("REGISTERED"),
    UNREGISTERED("UNREGISTERED");

    private final String status;

    UserStatus(String status){
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
