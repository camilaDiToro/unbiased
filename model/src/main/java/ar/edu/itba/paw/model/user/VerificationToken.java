package ar.edu.itba.paw.model.user;

import ar.edu.itba.paw.model.converter.LocalDateTimeConverter;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "email_verification_token")
public class VerificationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_verification_token_token_id_seq")
    @SequenceGenerator(name="email_verification_token_token_id_seq", sequenceName = "email_verification_token_token_id_seq", allocationSize = 1)
    @Column(name = "token_id")
    private long id;

    @Column(name = "token", nullable = false)
    private String token;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Convert(converter = LocalDateTimeConverter.class)
    @Column(name = "expiration_date", nullable = false)
    private LocalDateTime expiryDate;

    /* package */ VerificationToken(){
        // Just for hibernate
    }

    public VerificationToken(String token, long userId,final LocalDateTime expiryDate) {
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

    public void setId(long id) {
        this.id = id;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setExpiryDate(final LocalDateTime expiryDate) {
        this.expiryDate = expiryDate;
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