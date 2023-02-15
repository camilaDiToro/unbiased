package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.user.UserStatus;

public class StatusDto {

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    private String userStatus;

    public static StatusDto fromStatus(final UserStatus status){
        final StatusDto dto = new StatusDto();
        dto.userStatus = status.getStatus();
        return dto;
    }

}
