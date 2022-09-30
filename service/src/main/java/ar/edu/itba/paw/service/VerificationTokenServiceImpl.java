package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.VerificationToken;
import ar.edu.itba.paw.persistence.VerificationTokenDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class VerificationTokenServiceImpl implements VerificationTokenService{

    private static final int TOKEN_DURATION = 1;

    private final VerificationTokenDao verificationTokenDao;

    @Autowired
    public VerificationTokenServiceImpl(VerificationTokenDao verificationTokenDao) {
        this.verificationTokenDao = verificationTokenDao;
    }

    @Override
    public VerificationToken newToken(long userId) {
        final String token = UUID.randomUUID().toString();
        return verificationTokenDao.createEmailToken(userId, token, LocalDateTime.now().plusDays(TOKEN_DURATION));
    }

    @Override
    public Optional<VerificationToken> getToken(String token) {
        return verificationTokenDao.getEmailToken(token);
    }

    @Override
    public void deleteEmailToken(User user) {
        verificationTokenDao.deleteEmailToken(user);
    }
}
