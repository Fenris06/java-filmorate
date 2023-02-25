package ru.yandex.practicum.filmorate;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storege.users.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static java.util.Calendar.JULY;
import static java.util.Calendar.SEPTEMBER;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserStorageTest {
    @Autowired
    private UserStorage userStorage;

    @Test
    public void shouldUserStorageAddUserIfAllFieldsAreCorrect() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        final User testUser = userStorage.addUser(user);

        assertEquals(testUser.getEmail(), user.getEmail());
        assertEquals(testUser.getLogin(), user.getLogin());
        assertEquals(testUser.getName(), user.getName());
        assertEquals(testUser.getBirthday(), user.getBirthday());
    }

    @Test
    public void shouldUserStorageUpdateUserIfAllFieldsAreCorrect() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        final User testUser = userStorage.addUser(user);
        testUser.setEmail("Egor@rambler.ru");
        testUser.setLogin("Tor");
        testUser.setName("Egor");
        testUser.setBirthday(LocalDate.of(1958, JULY, 21));

        final User updateUser = userStorage.updateUser(testUser);

        assertEquals(testUser.getEmail(), updateUser.getEmail());
        assertEquals(testUser.getLogin(), updateUser.getLogin());
        assertEquals(testUser.getName(), updateUser.getName());
        assertEquals(testUser.getBirthday(), updateUser.getBirthday());
    }

    @Test
    public void shouldUserStorageGetAllUsers() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        User user1 = User.builder()
                .email("Egor@rambler.ru")
                .login("Tor")
                .name("Egor")
                .birthday(LocalDate.of(1958, JULY, 21))
                .build();

        userStorage.addUser(user);
        userStorage.addUser(user1);

        final List<User> users = userStorage.getUsers();

        assertEquals(users.size(), 4);
    }

    @Test
    public void shouldUserStorageGetUserFormId() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        userStorage.addUser(user);

        final User testUser = userStorage.getUser(user.getId());

        assertEquals(testUser.getEmail(), user.getEmail());
        assertEquals(testUser.getLogin(), user.getLogin());
        assertEquals(testUser.getName(), user.getName());
        assertEquals(testUser.getBirthday(), user.getBirthday());
    }

}
