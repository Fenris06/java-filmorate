package ru.yandex.practicum.filmorate;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storege.users.UserStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.util.Calendar.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserServiceTest {
    @Autowired
    private UserService userService;
    @MockBean
    private UserStorage userStorage;

    @Test
    public void shouldUserServesAddFriends() {
        Integer userFirstId = 1;
        Integer userSecondId = 2;
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        user.setId(userFirstId);

        User user1 = User.builder()
                .email("Egor@rambler.ru")
                .login("Tor")
                .name("Egor")
                .birthday(LocalDate.of(1958, JULY, 21))
                .build();

        user1.setId(userSecondId);
        Mockito.when(userStorage.getUser(userFirstId)).thenReturn(user);
        Mockito.when(userStorage.getUser(userSecondId)).thenReturn(user1);
        userService.addUserFriends(userFirstId, userSecondId);

        assertTrue(user.getFriends().contains(2));
        assertTrue(user1.getFriends().contains(1));
    }

    @Test
    public void shouldUserServesDeleteFriends() {
        Integer userFirstId = 1;
        Integer userSecondId = 2;
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        user.setId(userFirstId);
        user.getFriends().add(userSecondId);

        User user1 = User.builder()
                .email("Egor@rambler.ru")
                .login("Tor")
                .name("Egor")
                .birthday(LocalDate.of(1958, JULY, 21))
                .build();

        user1.setId(userSecondId);
        user1.getFriends().add(userFirstId);

        Mockito.when(userStorage.getUser(userFirstId)).thenReturn(user);
        Mockito.when(userStorage.getUser(userSecondId)).thenReturn(user1);
        userService.deleteUserFriends(userFirstId, userSecondId);

        assertEquals(user.getFriends().size(), 0);
        assertEquals(user1.getFriends().size(), 0);
    }

    @Test
    public void shouldUserServesGetUserFriendsList() {
        Integer userFirstId = 1;
        Integer userSecondId = 2;
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        user.setId(userFirstId);
        user.getFriends().add(userSecondId);

        User user1 = User.builder()
                .email("Egor@rambler.ru")
                .login("Tor")
                .name("Egor")
                .birthday(LocalDate.of(1958, JULY, 21))
                .build();

        user1.setId(userSecondId);
        user1.getFriends().add(userFirstId);

        Mockito.when(userStorage.getUser(userFirstId)).thenReturn(user);
        Mockito.when(userStorage.getUser(userSecondId)).thenReturn(user1);

        List<User> users = userService.getUserFriends(userFirstId);

        assertEquals(users.size(), 1);
        assertEquals(users.get(0), user1);
    }

    @Test
    public void shouldUserServesGetCommonFriendsList() {
        Integer userFirstId = 1;
        Integer userSecondId = 2;
        Integer userThirdId = 3;
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        user.setId(userFirstId);
        user.getFriends().add(userSecondId);
        user.getFriends().add(userThirdId);

        User user1 = User.builder()
                .email("Egor@rambler.ru")
                .login("Tor")
                .name("Egor")
                .birthday(LocalDate.of(1958, JULY, 21))
                .build();

        user1.setId(userSecondId);
        user1.getFriends().add(userFirstId);
        user1.getFriends().add(userThirdId);

        User user2 = User.builder()
                .email("Igor@rambler.ru")
                .login("Loki")
                .name("Igor")
                .birthday(LocalDate.of(1977, NOVEMBER, 22))
                .build();

        user2.setId(userThirdId);
        user2.getFriends().add(userFirstId);
        user2.getFriends().add(userSecondId);

        Mockito.when(userStorage.getUser(userFirstId)).thenReturn(user);
        Mockito.when(userStorage.getUser(userSecondId)).thenReturn(user1);
        Mockito.when(userStorage.getUser(userThirdId)).thenReturn(user2);

        List<User> commonFriends = userService.getCommonFriends(userFirstId, userSecondId);

        assertEquals(commonFriends.size(), 1);
        assertEquals(commonFriends.get(0), user2);

    }
}
