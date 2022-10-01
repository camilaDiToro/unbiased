package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
import ar.edu.itba.paw.model.user.VerificationToken;
import ar.edu.itba.paw.persistence.RoleDao;
import ar.edu.itba.paw.persistence.UserDao;
import ar.edu.itba.paw.persistence.VerificationTokenDao;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
@Transactional
public class UserServiceImplTest {
    private static final String USERNAME = "username";
    private static final String EMAIL = "user@mail.com";
    private static final String PASS = "userpass";
    private static final LocalDateTime DATE = LocalDateTime.now().plusDays(1);
    private static User.UserBuilder USER_BUILDER = new User.UserBuilder(EMAIL);
    @Mock
    private UserDao mockUserDao;
    @Mock
    private User mockUser;
    @Mock
    private VerificationTokenDao mockVerifDao;
    @Mock
    private RoleDao mockRoleDao;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private VerificationTokenService mockVerifService;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private ImageService mockImageService;
    @Mock
    private SecurityService mockSecurityService;

    @InjectMocks
    private UserServiceImpl userService;
            //new UserServiceImpl(mockUserDao, mockPasswordEncoder, mockEmailService, mockVerifService, mockRoleDao, mockImageService, mockSecurityService);

    @Before
    public void setTest() {
        LocaleContextHolder.setLocale(Locale.ENGLISH);
        mockUser = new User.UserBuilder(EMAIL).build();
    }

    @Test
    public void testCreate(){
        Mockito.when(mockUserDao.create(Mockito.eq(USER_BUILDER))).thenReturn(mockUser);

        try{
            User u = userService.create(USER_BUILDER);

            assertEquals(u.getId(), mockUser.getId());
            assertEquals(u.getEmail(), mockUser.getEmail());
        }
        catch ( UserNotFoundException e){
            fail("unexpected error during operation create user");
        }
    }

    @Test
    public void testResendUserVerificationInvalidEmail() {
        Mockito.when(mockUserDao.findByEmail(Mockito.eq("invalid@mail.com"))).thenThrow(new UserNotFoundException());
        try{
            userService.resendEmailVerification("invalid@mail.com");
        }
        catch (UserNotFoundException e){
            fail("unexpected error during operation resend email");
        }
    }


    @Test
    public void testVerifyUserMail(){
        VerificationToken token = new VerificationToken(1, "token", mockUser.getId(), DATE);
        Mockito.when(mockUserDao.create(Mockito.eq(USER_BUILDER))).thenReturn(mockUser);
        Mockito.when(mockVerifDao.getEmailToken(Mockito.anyString())).thenReturn((Optional.of(token)));
        Mockito.doNothing().when(mockUserDao).verifyEmail((Mockito.eq(mockUser.getId())));

        try {
            User user = userService.create(USER_BUILDER);
            VerificationToken.Status status = userService.verifyUserEmail("token");

            assertEquals(user.getStatus(), mockUser.getStatus());
            Mockito.verify(mockUserDao).verifyEmail(Mockito.anyLong());
        }catch (Exception e){
            fail("unexpected error during operation verify mail");
        }
    }

    @Test
    public void testUpdateProfile() {
        Mockito.when(mockUserDao.create(Mockito.eq(USER_BUILDER))).thenReturn(mockUser);
        Mockito.doNothing().when(mockUserDao).updateUsername(Mockito.eq(mockUser), Mockito.eq(USERNAME));

        try {
            User user = userService.create(USER_BUILDER);
            userService.updateProfile(user, USERNAME, null);

            assertEquals(user.getUsername(), mockUser.getUsername());
            Mockito.verify(mockUserDao).updateUsername(Mockito.any(User.class),Mockito.anyString());
        }
        catch (Exception e) {
            fail("unexpected error during operation update userprofile");
        }
    }

    /*@Test
    public void testCreateUser(){
        User user = new User(1, USERNAME, null);
        Mockito.when(userDao.create(Mockito.anyString())).thenReturn(user);

        try{
            User u = userService.create(USERNAME);
        }catch (Exception e){
            Assert.fail("unexpected error during operation create user");
        }
    */
}

