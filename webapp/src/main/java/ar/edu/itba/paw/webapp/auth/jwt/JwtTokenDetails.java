package ar.edu.itba.paw.webapp.auth.jwt;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public final class JwtTokenDetails {

    private final String id;
    private final String email;
    private final List<String> authorities;
    private final Date issuedDate;
    private final Date expirationDate;
    private final String token;
    private final JwtTokenType tokenType;

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getAuthorities() {
        return authorities;
    }

    public Date getIssuedDate() {
        return issuedDate;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    private JwtTokenDetails(String id, String email, List<String> authorities, Date issuedDate, Date expirationDate, String token,JwtTokenType tokenType) {
        this.id = id;
        this.email = email;
        this.authorities = authorities;
        this.issuedDate = issuedDate;
        this.expirationDate = expirationDate;
        this.token = token;
        this.tokenType = tokenType;
    }

    public String getToken() {
        return token;
    }

    public JwtTokenType getTokenType() {
        return tokenType;
    }


    public static class Builder {

        private String id;
        private String email;
        private List<String> authorities;
        private Date issuedDate;
        private Date expirationDate;
        private String token;
        private JwtTokenType tokenType;

        public Builder withId(String id) {
            this.id = id;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withAuthorities(List<String> authorities) {
            this.authorities = Collections.unmodifiableList(authorities == null ? Collections.<String>emptyList() : authorities);
            return this;
        }

        public Builder withIssuedDate(Date issuedDate) {
            this.issuedDate = issuedDate;
            return this;
        }

        public Builder withExpirationDate(Date expirationDate) {
            this.expirationDate = expirationDate;
            return this;
        }

        public Builder withToken(String token) {
            this.token = token;
            return this;
        }

        public Builder withTokenType(JwtTokenType tokenType) {
            this.tokenType = tokenType;
            return this;
        }


        public JwtTokenDetails build() {
            return new JwtTokenDetails(id, email, authorities, issuedDate, expirationDate, token, tokenType);
        }
    }
}
