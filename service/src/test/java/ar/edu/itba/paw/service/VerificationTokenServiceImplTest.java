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
            Assert.fail("unexpected error during operation create token");

        }
    }

    @Test
    public void testGetToken() {
        Mockito.when(mockverificationDao.createEmailToken(Mockito.eq(mockUser.getId()), Mockito.eq(TOKEN), Mockito.eq(DATE)))
                .thenReturn(mockVT);
        Mockito.when(mockverificationDao.getEmailToken(Mockito.eq(TOKEN))).thenReturn(Optional.of(mockVT));

        try {
            Optional<VerificationToken> token = tokenService.getToken(TOKEN);
            Assert.assertEquals(token.get().getToken(), mockVT.getToken());
        }
        catch (Exception e){
            Assert.fail("unexpected error during operation get token");
        }
    }

    /*@Test
    public void testIsInvalidToken() {
        LocalDateTime expiryDate = LocalDateTime.now().plusDays(-1);
        VerificationToken token = new VerificationToken(1, TOKEN,mockUser.getId(), expiryDate);

        Optional<VerificationToken> optionalVerificationToken = tokenService.getToken(token.getToken());
        Assert.assertTrue(optionalVerificationToken.isPresent());
        boolean isValid = optionalVerificationToken.get().isValidToken();

        Assert.assertFalse(isValid);
    }*/


}
