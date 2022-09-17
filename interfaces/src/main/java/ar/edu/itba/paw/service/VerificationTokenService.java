package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.VerificationToken;

import java.util.Optional;

public interface VerificationTokenService {
    VerificationToken newToken(long userId);
    Optional<VerificationToken> getToken(String token);
    void deleteEmailToken(long userId);
}