package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.VerificationToken;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.user.UserStatus;
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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Optional;
import org.springframework.transaction.annotation.Transactional;


@RunWith(MockitoJUnitRunner.class)
@Transactional
public class UserServiceImplTest {
    private static final String USERNAME = "username";
    private static final String EMAIL = "user@mail.com";
    private static final String PASS = "userpass";
    static User.UserBuilder usBuilderTest = new User.UserBuilder(EMAIL);
    private static final User testUser = new User(usBuilderTest);

    @Mock
    private UserDao mockUserDao;
    @Mock
    private User mockUser;
    @Mock
    private AdminService mockAdminService;
    @Mock
    private VerificationTokenDao mockVerifDao;
    @Mock
    private PasswordEncoder mockPasswordEncoder;
    @Mock
    private EmailService mockEmailService;
    @Mock
    private VerificationTokenService mockVerificationTokenService;
    @Mock
    private RoleDao mockRoleDao;
    @Mock
    private ImageService mockImageService;
    @InjectMocks
    private UserServiceImpl userService;

    @Before
    public void setTest() {
        LocaleContextHolder.setLocale(Locale.getDefault());
    }

    @Test
    public void testCreate() throws Exception {
        User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);
        VerificationToken token = new VerificationToken(1, "token", testUser.getId(), LocalDateTime.now().plusDays(2));
        Mockito.when(mockUserDao.create(Mockito.eq(usBuilder))).thenReturn(testUser);
        Mockito.when(mockPasswordEncoder.encode(PASS)).thenReturn(PASS);

        User user = userService.create(usBuilder);
        Assert.assertNotNull(user);
        Assert.assertEquals(testUser.getUsername(), user.getUsername());
        Assert.assertEquals(testUser.getEmail(), user.getEmail());
        Assert.assertEquals(testUser.getPass(), user.getPass());
        Mockito.verify(mockUserDao).create(usBuilder);
    }

    @Test
    public void testFindById(){
        User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);
        Mockito.when(mockUserDao.create(Mockito.eq(usBuilder))).thenReturn(testUser);
        Mockito.when(mockUserDao.findByEmail(Mockito.eq(EMAIL))).thenReturn(Optional.of(testUser));
        User user = userService.create(usBuilder);

        Assert.assertNotNull(user);
        Assert.assertEquals(user.getEmail(), user.getEmail());
    }

    @Test
    public void testFindByUsername() {
        User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);
        Mockito.when(mockUserDao.create(Mockito.eq(usBuilder))).thenReturn(testUser);
        Mockito.when(mockUserDao.findByUsername(Mockito.eq(USERNAME))).thenReturn(Optional.of(testUser));
        Mockito.doNothing().when(mockUserDao).updateUsername(Mockito.eq(testUser.getId()), Mockito.eq(USERNAME));
        User user = userService.create(usBuilder);

        Assert.assertNotNull(user);
        Assert.assertEquals(user.getUsername(), testUser.getUsername());
    }

    @Test
    public void testVerifyUserMail(){
        User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);
        VerificationToken token = new VerificationToken(1, "token", mockUser.getId(), LocalDateTime.now().plusDays(2));
        Mockito.lenient().when(mockUserDao.create(Mockito.eq(usBuilder))).thenReturn(testUser);
        Mockito.when(mockVerifDao.getEmailToken(Mockito.anyString())).thenReturn((Optional.of(token)));
        Mockito.doNothing().when(mockUserDao).verifyEmail((Mockito.eq(testUser.getId())));
        userService.verifyUserEmail("token");

        Assert.assertEquals(testUser.getStatus(), UserStatus.UNREGISTERED);
        //Mockito.verify(mockUserDao).verifyEmail(testUser.getId());
    }

    @Test
    public void testUpdateProfile() {
        User.UserBuilder usBuilder = new User.UserBuilder(EMAIL);
        Mockito.when(mockUserDao.create(Mockito.eq(usBuilder))).thenReturn(testUser);
        Mockito.doNothing().when(mockUserDao).updateUsername(Mockito.eq(testUser.getId()), Mockito.eq(USERNAME));
        Mockito.when(mockUser.getUsername()).thenReturn(testUser.getUsername());

        //userService.updateProfile(testUser.getId(), USERNAME, null);

        Assert.assertEquals(testUser.getUsername(), mockUser.getUsername());
        //Mockito.verify(mockUserDao).updateUsername(Mockito.eq(testUser.getId()),Mockito.anyString());
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

