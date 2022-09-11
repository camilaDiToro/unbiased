package ar.edu.itba.paw.model;

import java.time.LocalDateTime;

public class VerificationToken {

    private static final int EXPIRATION_DAYS = 1;

    private final long id;
    private final String token;
    private final long userId;
    private final LocalDateTime expiryDate;

    public VerificationToken(long id, String token, long userId, LocalDateTime expiryDate) {
        this.id = id;
        this.token = token;
        this.userId = userId;
        this.expiryDate = expiryDate;
    }

    public long getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    public long getUserId() {
        return userId;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }

    public static LocalDateTime newExpiryDate() {
        return LocalDateTime.now().plusDays(EXPIRATION_DAYS);
    }

    public boolean isValidToken() {
        return LocalDateTime.now().isBefore(expiryDate);
    }
}