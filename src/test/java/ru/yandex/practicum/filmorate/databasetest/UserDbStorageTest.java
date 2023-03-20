package ru.yandex.practicum.filmorate.databasetest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storege.films.FilmDbStorage;
import ru.yandex.practicum.filmorate.storege.users.UserDbStorage;

import java.time.LocalDate;
import java.time.Month;
import java.util.List;

import static java.util.Calendar.*;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;

    @Test
    public void shouldUserDbStorageAddUser() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        User newUser = userDbStorage.addUser(user);
        assertEquals(user, newUser);
    }

    @Test
    public void shouldUserDbStorageUpdateUser() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        final User testUser = userDbStorage.addUser(user);
        testUser.setEmail("Egor@rambler.ru");
        testUser.setLogin("Tor");
        testUser.setName("Egor");
        testUser.setBirthday(LocalDate.of(1958, JULY, 21));

        final User updateUser = userDbStorage.updateUser(testUser);

        assertEquals(testUser, updateUser);
    }

    @Test
    public void shouldUserDbStorageGetAllUsers() {
        List<User> users = userDbStorage.getUsers();
        assertEquals(users.size(), 8);
    }

    @Test
    public void shouldUserDbStorageGetUserFromId() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        Integer userId = userDbStorage.addUser(user).getId();
        User testUser = userDbStorage.getUser(userId);

        assertEquals(user, testUser);
    }

    @Test
    public void shouldUserDbStorageAddUserFriendAndGetFriend() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();
        User friendUser = User.builder()
                .email("Egor@rambler.ru")
                .login("Tor")
                .name("Egor")
                .birthday(LocalDate.of(1990, FEBRUARY, 12))
                .build();

        Integer userId = userDbStorage.addUser(user).getId();
        Integer friendUserId = userDbStorage.addUser(friendUser).getId();

        userDbStorage.addUserFriends(userId, friendUserId);

        List<User> testFriend = userDbStorage.getUserFriends(userId);

        assertEquals(testFriend.size(), 1);
        assertEquals(testFriend.get(0).getEmail(), friendUser.getEmail());
        assertEquals(testFriend.get(0).getBirthday(), friendUser.getBirthday());
    }

    @Test
    public void shouldUserDbStorageDeleteFriend() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();
        User friendUser = User.builder()
                .email("Egor@rambler.ru")
                .login("Tor")
                .name("Egor")
                .birthday(LocalDate.of(1990, MARCH, 12))
                .build();

        Integer userId = userDbStorage.addUser(user).getId();
        Integer friendUserId = userDbStorage.addUser(friendUser).getId();

        userDbStorage.addUserFriends(userId, friendUserId);
        userDbStorage.deleteUserFriends(userId, friendUserId);

        List<User> testFriend = userDbStorage.getUserFriends(userId);

        assertEquals(testFriend.size(), 0);
    }

    @Test
    public void shouldUsrDbStorageGetCommonFriends() {
        User user1 = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();
        User user2 = User.builder()
                .email("Egor@rambler.ru")
                .login("Tor")
                .name("Egor")
                .birthday(LocalDate.of(1990, MARCH, 12))
                .build();
        User user3 = User.builder()
                .email("Ivan@rambler.ru")
                .login("Loki")
                .name("Ivan")
                .birthday(LocalDate.of(1987, APRIL, 12))
                .build();

        Integer user1Id = userDbStorage.addUser(user1).getId();
        Integer user2Id = userDbStorage.addUser(user2).getId();
        Integer user3Id = userDbStorage.addUser(user3).getId();

        userDbStorage.addUserFriends(user1Id, user3Id);
        userDbStorage.addUserFriends(user2Id, user3Id);

        List<User> commonFriends = userDbStorage.getCommonFriends(user1Id, user2Id);

        assertEquals(commonFriends.size(), 1);
        assertEquals(commonFriends.get(0).getEmail(), user3.getEmail());
        assertEquals(commonFriends.get(0).getBirthday(), user3.getBirthday());
    }


}
