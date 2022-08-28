package ar.edu.itba.paw.model;

public class User {

    private final long id;
    private final String email;
    private final Long dataId;

    public User(long id, String email, Long dataId) {
        this.id = id;
        this.email = email;
        this.dataId = dataId;
    }

    public long getId() {
        return id;
    }

    public long getDataId() {
        return dataId;
    }

    public String getEmail() {
        return email;
    }

}
