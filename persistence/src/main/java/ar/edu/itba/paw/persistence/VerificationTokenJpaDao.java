package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.VerificationToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public class FVerificationTokenJpaDao implements VerificationTokenDao{

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationTokenJpaDao.class);

    @Override
    public VerificationToken createEmailToken(long userId, String token, LocalDateTime expiryDate) {
        VerificationToken verificationToken = new VerificationToken(token, userId, expiryDate);
        entityManager.persist(verificationToken);
        LOGGER.debug("Verification token {} with id {} created for user with id {}", verificationToken.getToken(), verificationToken.getId(), verificationToken.getUserId());
        return verificationToken;
    }

    @Override
    public Optional<VerificationToken> getEmailToken(String token) {
        final TypedQuery<VerificationToken> query = entityManager.createQuery("from VerificationToken as vt WHERE vt.token = :token",VerificationToken.class)
                .setParameter("token", token);
        return query.getResultList().stream().findFirst();
    }

    @Override
    public void deleteEmailToken(long userId) {
        final Optional<VerificationToken> mayBeVt= entityManager.createQuery("from VerificationToken as vt WHERE vt.userId = :userId",VerificationToken.class)
                .setParameter("userId", userId).getResultList().stream().findFirst();
        mayBeVt.ifPresent(verificationToken -> entityManager.remove(verificationToken));
    }
}
