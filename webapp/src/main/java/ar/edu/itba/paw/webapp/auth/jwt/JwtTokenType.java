package ar.edu.itba.paw.webapp.auth.jwt;

import ar.edu.itba.paw.model.user.UserStatus;

public enum JwtTokenType {

    REFRESH("refresh"),
    ACCESS("access");

    private final String type;

    JwtTokenType(String type){
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public static JwtTokenType getByType(String type){
        for (JwtTokenType c : JwtTokenType.values()) {
            if (c.getType().equals(type) ) {
                return c;
            }
        }
        return null;
    }
}
