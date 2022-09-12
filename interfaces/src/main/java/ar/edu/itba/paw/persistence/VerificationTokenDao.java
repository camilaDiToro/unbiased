package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.service.VerificationTokenService;

import java.time.LocalDateTime;
import java.util.Optional;

public interface VerificationTokenDao {
    VerificationToken create(long userId, String token, LocalDateTime expiryDate);
    Optional<VerificationToken> getToken(String token);
}
