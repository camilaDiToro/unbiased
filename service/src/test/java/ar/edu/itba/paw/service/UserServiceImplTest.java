package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import ar.edu.itba.paw.model.user.VerificationToken;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.persistence.VerificationTokenDao;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    //User
    private static final String USERNAME = "username";
    private static final String EMAIL = "user@mail.com";
    private static final String PASS = "userpass";
    private static final long ID = 1;
    private static final long OTHER_ID = 1;


    //TOKEN
    private static final String TOKEN = "A1234";
    @Mock
    private VerificationToken mockVerificationToken;

    @Mock
    private User mockUser;

    @Mock
    private User.UserBuilder mockUserBuilder;
    @Mock
    private UserDao mockUserDao;

    @Mock
    private EmailService emailService;

    @Mock
    private VerificationTokenService mockVerificationTokenService;


    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testGetRegisteredUserById(){
        Mockito.when(mockUserDao.getUserById(Mockito.eq(ID))).thenReturn(Optional.of(mockUser));

        Optional<User> mayBeUser = userService.getUserById(ID);

        assertTrue(mayBeUser.isPresent());
    }

    @Test
    public void testGetUserById(){
        Mockito.when(mockUserDao.getRegisteredUserById(Mockito.eq(ID))).thenReturn(Optional.of(mockUser));

        Optional<User> mayBeUser = userService.getRegisteredUserById(ID);

        assertTrue(mayBeUser.isPresent());
    }


    @Test
    public void testGetRegisteredUserByIdGetUnregistered(){

        Optional<User> mayBeUser = userService.getRegisteredUserById(OTHER_ID);

        assertFalse(mayBeUser.isPresent());
    }

    @Test
    public void testCreate(){
        Mockito.when(mockUserDao.create(Mockito.eq(mockUserBuilder))).thenReturn(mockUser);
        Mockito.when(mockUser.getId()).thenReturn(ID);

        User u = userService.create(mockUserBuilder);
        assertNotNull(u);
        assertEquals(u.getId(), ID);
    }

    @Test
    public void testVerifyUserEmailInvalidToken(){
        Mockito.when(mockVerificationToken.isValidToken()).thenReturn(false);
        Mockito.when(mockVerificationTokenService.getToken(Mockito.eq(TOKEN))).thenReturn(Optional.of(mockVerificationToken));


        VerificationToken.Status vt = userService.verifyUserEmail(TOKEN);

        assertEquals(VerificationToken.Status.EXPIRED, vt);
    }

    @Test
    public void testVerifyUserEmailValidToken(){
        Mockito.when(mockVerificationToken.isValidToken()).thenReturn(true);
        Mockito.when(mockVerificationToken.getUserId()).thenReturn(ID);
        Mockito.when(mockVerificationTokenService.getToken(Mockito.eq(TOKEN))).thenReturn(Optional.of(mockVerificationToken));

        Mockito.when(mockUserDao.getUserById(Mockito.eq(ID))).thenReturn(Optional.of(mockUser));

        VerificationToken.Status vt = userService.verifyUserEmail(TOKEN);

        assertEquals(VerificationToken.Status.SUCCESFFULLY_VERIFIED, vt);
    }

    @Test
    public void testResendUserVerificationInvalidEmail() {
        Mockito.when(mockUserDao.findByEmail(Mockito.eq(EMAIL))).thenReturn(Optional.empty());
        assertEquals(VerificationToken.Status.NOT_EXISTS, userService.resendEmailVerification(EMAIL));
    }

    @Test
    public void testResendUserVerificationAlreadyVerified() {
        Mockito.when(mockUserDao.findByEmail(Mockito.eq(EMAIL))).thenReturn(Optional.of(mockUser));
        Mockito.when(mockUser.getStatus()).thenReturn(UserStatus.REGISTERED);

        assertEquals(VerificationToken.Status.ALREADY_VERIFIED, userService.resendEmailVerification(EMAIL));
    }

    @Test
    public void testResendUserVerificationSuccessfullyResended() {
        Mockito.when(mockUserDao.findByEmail(Mockito.eq(EMAIL))).thenReturn(Optional.of(mockUser));
        Mockito.when(mockUser.getStatus()).thenReturn(UserStatus.UNABLE);

        assertEquals(VerificationToken.Status.SUCCESSFULLY_RESENDED, userService.resendEmailVerification(EMAIL));
    }

}

