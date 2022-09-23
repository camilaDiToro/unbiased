package ar.edu.itba.paw.service;

import ar.edu.itba.paw.persistence.UserDao;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final String USERNAME = "user1";

    @InjectMocks
    private UserServiceImpl userService;
    @Mock
    private UserDao userDao;

    @Test
    public void testCreateUser(){
        /*User user = new User(1, USERNAME, null);
        Mockito.when(userDao.create(Mockito.anyString())).thenReturn(user);

        try{
            User u = userService.create(USERNAME);
        }catch (Exception e){
            Assert.fail("unexpected error during operation create user");
        }*/

    }

}
