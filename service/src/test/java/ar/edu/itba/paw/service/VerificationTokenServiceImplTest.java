package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.VerificationToken;
import ar.edu.itba.paw.persistence.VerificationTokenDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

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

        VerificationToken token = tokenService.newToken(mockVT.getUserId());
        Assert.assertEquals(TOKEN, token.getToken());
    }

    @Test
    public void testGetToken() {
        Mockito.when(mockverificationDao.getEmailToken(mockVT.getToken()))
                .thenReturn(Optional.of(mockVT));

        Optional<VerificationToken> token = tokenService.getToken(TOKEN);
        Assert.assertEquals(token.get().getToken(), mockVT.getToken());
    }
}
