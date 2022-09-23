package ar.edu.itba.paw.model.user;

import ar.edu.itba.paw.model.PositivityStats;

public class User {

    private final long userId;
    private final Long imageId;
    private final String email, username, pass;
    private final UserStatus status;

    private final PositivityStats positivityStats;

    public User(User.UserBuilder userBuilder) {
        this.userId = userBuilder.userId;
        this.email = userBuilder.email;
        this.imageId = userBuilder.imageId;
        this.username = userBuilder.username;
        this.pass = userBuilder.pass;
        this.status = userBuilder.status;
        this.positivityStats = userBuilder.positivity;
    }

    @Override
    public String toString() {
        return username != null ? username : email;
    }


    public long getId() {
        return userId;
    }

    public Long getImageId() {
        return imageId;
    }

    public boolean hasImage() {
        return imageId != null;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public PositivityStats getPositivityStats() {
        return positivityStats;
    }

    public String getPass() {
        return pass;
    }

    public UserStatus getStatus() {
        return status;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof User))
            return false;

        User aux = (User) o;

        return aux.userId == userId;
    }

    public static class UserBuilder{
        private long userId;
        private Long imageId;
        private final String email;
        private String username, pass;
        private UserStatus status;

        private PositivityStats positivity;

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

        public UserBuilder imageId(Long imageId){
            this.imageId = imageId;
            return this;
        }

        public UserBuilder username(String username){
            this.username = username;
            return this;
        }

        public UserBuilder pass(String pass){
            this.pass = pass;
            status = UserStatus.UNABLE;
            return this;
        }

        public UserBuilder status(String status){
            this.status = UserStatus.valueOf(status);
            return this;
        }

        public UserBuilder positivity(PositivityStats positivity){
            this.positivity = positivity;
            return this;
        }

        public User build(){
            return new User(this);
        }

        public long getUserId() {
            return userId;
        }

        public Long getImageId() {
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
