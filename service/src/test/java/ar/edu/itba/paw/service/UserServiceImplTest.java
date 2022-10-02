package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImplTest.class);

    @InjectMocks
    private UserServiceImpl userService;
            //new UserServiceImpl(mockUserDao, mockPasswordEncoder, mockEmailService, mockVerifService, mockRoleDao, mockImageService, mockSecurityService);

    private VerificationTokenService verificationTokenService;
    @Before
    public void setTest() {
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
             LOGGER.warn("Unexpected error during operation create user test threw exception");
        }
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
    public void testVerifyUserMail(){
        VerificationToken token = new VerificationToken(1, "token", mockUser.getId(), DATE);
        Mockito.when(mockUserDao.create(Mockito.eq(USER_BUILDER))).thenReturn(mockUser);
        //Mockito.when(mockVerifDao.getEmailToken(Mockito.anyString())).thenReturn((Optional.of(token)));
        Mockito.doNothing().when(mockUserDao).verifyEmail((Mockito.eq(mockUser.getId())));

        try {
            User user = userService.create(USER_BUILDER);
            Optional<VerificationToken> mayBeBt = verificationTokenService.getToken("token");
            VerificationToken vt = mayBeBt.get();
            User us = mockUserDao.getUserById(vt.getUserId()).get();
            mockUserDao.verifyEmail(us.getId());
            VerificationToken.Status status = userService.verifyUserEmail(mayBeBt.get().getToken());


            assertEquals(VerificationToken.Status.SUCCESFFULLY_VERIFIED, status);
            Mockito.verify(mockUserDao).verifyEmail(Mockito.anyLong());
        }catch (Exception e){
            LOGGER.warn("Unexpected error during operation verify mail test threw exception");
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
            LOGGER.warn("Unexpected error during operation update profile test threw exception");
        }
    }

    @Test
    public void testAddRole(){
        List<String> listRole = new ArrayList<>();
        Mockito.when(mockUserDao.create(Mockito.eq(USER_BUILDER))).thenReturn(mockUser);
        //Mockito.doNothing().when(mockRoleDao).addRole(Mockito.eq(mockUser.getId()), Role.JOURNALIST);
        //Mockito.when(mockRoleDao.getRoles(Mockito.eq(mockUser.getId()))).thenReturn(listRole);

        try{
            User user = userService.create(USER_BUILDER);
            //User optionalUser = userService.getUserById(user.getId()).get();
            userService.addRole(user, Role.JOURNALIST);
            List<Role> roleList = userService.getRoles(user);

            assertEquals(roleList.size(), 1);
        }
        catch ( UserNotFoundException e){
            LOGGER.warn("Unexpected error during operation add role test threw exception");
        }
    }
    /*
    @Test
    public void testVerifyAccount(){
        VerificationToken token = new VerificationToken("token", mockUser, LocalDate.now().plusDays(2));
        when(mockVerifDao.getTokenByStringValue(Mockito.anyString())).thenReturn((Optional.of(token)));
        Mockito.doNothing().when(mockVerifDao).removeTokenByUserId(Mockito.any(User.class));

        userService.verifyAccount("token");
        verify(mockRoleService).addRole(mockUser, Role.VERIFIED);
        verify(mockRoleService).removeRole(mockUser, Role.UNVERIFIED);
    }
     */

}

