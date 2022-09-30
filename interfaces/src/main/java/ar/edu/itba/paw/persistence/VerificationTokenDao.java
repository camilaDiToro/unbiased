package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.VerificationToken;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationTokenDao {
    VerificationToken createEmailToken(long userId, String token, LocalDateTime expiryDate);
    Optional<VerificationToken> getEmailToken(String token);
    void deleteEmailToken(User user);
}
