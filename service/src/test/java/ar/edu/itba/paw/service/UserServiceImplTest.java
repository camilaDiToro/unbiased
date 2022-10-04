package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import ar.edu.itba.paw.model.user.VerificationToken;
import ar.edu.itba.paw.persistence.RoleDao;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.persistence.VerificationTokenDao;
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
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class UserServiceImplTest {

    //User
    private static final String USERNAME = "username";
    private static final String EMAIL = "user@mail.com";
    private static final String PASS = "userpass";
    private static final long ID = 1;
    private User user;

    //TOKEN
    private static final String TOKEN = "A1234";
    private static final long TOKEN_ID = 5;
    @Mock
    private VerificationToken mockVerificationToken;

    private static final LocalDateTime DATE = LocalDateTime.now().plusDays(1);
    private static User.UserBuilder USER_BUILDER = new User.UserBuilder(EMAIL);
    @Mock
    private UserDao mockUserDao;


    @Mock
    private VerificationTokenDao mockVerifDao;
    @Mock
    private RoleDao mockRoleDao;

    @Mock
    private EmailService mockEmailService;
    @Mock
    private VerificationTokenService mockVerificationTokenService;

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImplTest.class);

    @InjectMocks
    private UserServiceImpl userService;

    /*@Before
    public void setTest() {

    }*/

    @Test
    public void testGetRegisteredUserByIdGetRegistered(){
        user = new User.UserBuilder(EMAIL).userId(ID).status(UserStatus.REGISTERED.getStatus()).build();
        Mockito.when(mockUserDao.getUserById(Mockito.eq(ID))).thenReturn(Optional.of(user));

        Optional<User> mayBeUser = userService.getRegisteredUserById(ID);

        assertTrue(mayBeUser.isPresent());
    }

    @Test
    public void testGetRegisteredUserByIdGetUnregistered(){
        user = new User.UserBuilder(EMAIL).userId(ID).status(UserStatus.UNABLE.getStatus()).build();
        Mockito.when(mockUserDao.getUserById(Mockito.eq(ID))).thenReturn(Optional.of(user));

        Optional<User> mayBeUser = userService.getRegisteredUserById(ID);

        assertFalse(mayBeUser.isPresent());
    }

    @Test
    public void testCreate(){
        user = new User.UserBuilder(EMAIL).userId(ID).username(USERNAME).build();
        Mockito.when(mockUserDao.create(Mockito.eq(USER_BUILDER))).thenReturn(user);

        User u = userService.create(USER_BUILDER);

        assertNotNull(u);
        assertEquals(u.getId(), user.getId());
        assertEquals(u.getEmail(), user.getEmail());
    }

    @Test(expected = UserNotFoundException.class)
    public void testVerifyUserEmailInvalidUser(){

        Mockito.when(mockVerificationToken.isValidToken()).thenReturn(false);
        Mockito.when(mockVerificationToken.getUserId()).thenReturn(ID);
        Mockito.when(mockVerificationTokenService.getToken(Mockito.eq(TOKEN))).thenReturn(Optional.of(mockVerificationToken));
        Mockito.when(mockUserDao.getUserById(Mockito.eq(ID))).thenReturn(Optional.empty());

        VerificationToken.Status vt = userService.verifyUserEmail(TOKEN);

        Assert.fail("Should have thrown UserNotFoundException");
    }

    @Test
    public void testVerifyUserEmailInvalidToken(){
        Mockito.when(mockVerificationToken.isValidToken()).thenReturn(false);
        Mockito.when(mockVerificationToken.getUserId()).thenReturn(ID);
        Mockito.when(mockVerificationTokenService.getToken(Mockito.eq(TOKEN))).thenReturn(Optional.of(mockVerificationToken));

        user = new User.UserBuilder(EMAIL).userId(ID).username(USERNAME).build();
        Mockito.when(mockUserDao.getUserById(Mockito.eq(ID))).thenReturn(Optional.of(user));

        VerificationToken.Status vt = userService.verifyUserEmail(TOKEN);

        assertEquals(VerificationToken.Status.EXPIRED, vt);
    }

    @Test
    public void testVerifyUserEmailValidToken(){
        Mockito.when(mockVerificationToken.isValidToken()).thenReturn(true);
        Mockito.when(mockVerificationToken.getUserId()).thenReturn(ID);
        Mockito.when(mockVerificationTokenService.getToken(Mockito.eq(TOKEN))).thenReturn(Optional.of(mockVerificationToken));

        user = new User.UserBuilder(EMAIL).userId(ID).username(USERNAME).build();
        Mockito.when(mockUserDao.getUserById(Mockito.eq(ID))).thenReturn(Optional.of(user));

        VerificationToken.Status vt = userService.verifyUserEmail(TOKEN);

        assertEquals(VerificationToken.Status.SUCCESFFULLY_VERIFIED, vt);
    }

    @Test
    public void testResendUserVerificationInvalidEmail() {
        Mockito.when(mockUserDao.findByEmail(Mockito.eq("invalid@mail.com"))).thenThrow(new UserNotFoundException());
        try{
            userService.resendEmailVerification("invalid@mail.com");
        }
        catch (UserNotFoundException e){
            LOGGER.warn("Unexpected error during operation resend email test threw exception");
        }
    }

    @Test
    public void testUpdateProfile() {
        Mockito.when(mockUserDao.create(Mockito.eq(USER_BUILDER))).thenReturn(user);
        Mockito.doNothing().when(mockUserDao).updateUsername(Mockito.eq(user), Mockito.eq(USERNAME));

        User user = userService.create(USER_BUILDER);
        userService.updateProfile(user, USERNAME, null);

        assertEquals(user.getUsername(), user.getUsername());
        Mockito.verify(mockUserDao).updateUsername(Mockito.any(User.class),Mockito.anyString());
    }

}

