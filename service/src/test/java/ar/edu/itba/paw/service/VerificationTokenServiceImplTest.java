package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.VerificationToken;
import ar.edu.itba.paw.persistence.VerificationTokenDao;
import jdk.nashorn.internal.parser.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class VerificationTokenServiceImplTest {
    @Mock
    private User mockUser;
    @Mock
    private VerificationToken mockVT;
    private static final String EMAIL = "user@email.com";

    private static final String TOKEN = "token";
    private static final LocalDateTime DATE = LocalDateTime.now().plusDays(1);
    private static final Logger LOGGER = LoggerFactory.getLogger(VerificationTokenServiceImplTest.class);

    @InjectMocks
    private VerificationTokenServiceImpl tokenService;
    @Mock
    private VerificationTokenDao mockverificationDao;

    @Before
    public void setup() {
        mockUser = new User.UserBuilder(EMAIL).build();
        mockVT = new VerificationToken(1, TOKEN, mockUser.getId(), DATE);
    }

    @Test
    public void testCreateToken() {
        Mockito.when(mockverificationDao.createEmailToken(Mockito.anyLong(), Mockito.anyString(), Mockito.any()))
                .thenReturn(mockVT);

        try {
            VerificationToken token = tokenService.newToken(mockVT.getUserId());
            Assert.assertEquals(TOKEN, token.getToken());
        }
        catch (Exception e){
            LOGGER.warn("Unexpected error during operation create token test threw exception");
        }
    }

    @Test
    public void testGetToken() {
        Mockito.when(mockverificationDao.createEmailToken(Mockito.eq(mockUser.getId()), Mockito.eq(TOKEN), Mockito.eq(DATE)))
                .thenReturn(mockVT);

        try {
            Optional<VerificationToken> token = tokenService.getToken(TOKEN);
            Assert.assertEquals(token.get().getToken(), mockVT.getToken());
        }
        catch (Exception e){
            LOGGER.warn("Unexpected error during operation get token test threw exception");
        }
    }
}
