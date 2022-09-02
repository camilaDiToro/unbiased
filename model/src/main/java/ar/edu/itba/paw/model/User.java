package ar.edu.itba.paw.model;

public class User {

    private final long userId, imageId;
    private final String email, username, pass;
    private final UserStatus status;

    public User(User.UserBuilder userBuilder) {
        this.userId = userBuilder.userId;
        this.email = userBuilder.email;
        this.imageId = userBuilder.imageId;
        this.username = userBuilder.username;
        this.pass = userBuilder.pass;
        this.status = userBuilder.status;
    }

    public long getId() {
        return userId;
    }

    public long getImageId() {
        return imageId;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPass() {
        return pass;
    }

    public UserStatus getStatus() {
        return status;
    }

    public static class UserBuilder{
        private long userId, imageId;
        private final String email;
        private String username, pass;
        private UserStatus status;

        public UserBuilder(String email){
            if(email == null){
                throw new NullPointerException("User email must not be null");
            }
            this.email = email;
            status = UserStatus.UNREGISTERED;
        }

        public UserBuilder userId(long userId){
            this.userId = userId;
            return this;
        }

        public UserBuilder imageId(long imageId){
            this.imageId = imageId;
            return this;
        }

        public UserBuilder username(String username){
            this.username = username;
            return this;
        }

        public UserBuilder pass(String pass){
            this.pass = pass;
            status = UserStatus.REGISTERED;
            return this;
        }

        public User build(){
            return new User(this);
        }

        public long getUserId() {
            return userId;
        }

        public long getImageId() {
            return imageId;
        }

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }

        public String getPass() {
            return pass;
        }

        public UserStatus getStatus() {
            return status;
        }
    }

}
