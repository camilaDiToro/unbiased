package ar.edu.itba.paw.model;

import java.time.LocalDateTime;

public class VerificationToken {

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

    public boolean isValidToken() {
        return LocalDateTime.now().isBefore(expiryDate);
    }

    public enum Status{
        SUCCESFFULLY_VERIFIED("SUCCESFULLY_VERIFIED", "verificationToken.succesfullyVerified"),
        SUCCESSFULLY_RESENDED("SUCCESSFULLY_RESENDED","verificationToken.succesfullyResended"),
        EXPIRED("EXPIRED","verificationToken.expired"),
        NOT_EXISTS("NOT_EXISTS","verificationToken.notExists"),
        ALREADY_VERIFIED("ALREADY_VERIFIED","verificationToken.alreadyVerified");

        private final String status;
        private final String code;

        Status(String status, String code){
            this.status = status;
            this.code = code;
        }

        public String getStatus() {
            return status;
        }

        public String getCode() {
            return code;
        }
    }
}