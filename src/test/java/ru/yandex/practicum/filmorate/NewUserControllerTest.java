package ru.yandex.practicum.filmorate;

import org.junit.After;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storege.users.UserStorage;


import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Objects;
import java.util.Optional;

import java.util.List;


import static java.util.Calendar.SEPTEMBER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewUserControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private UserStorage userStorage;
    @Autowired
    private UserService userService;

    @After
    public void afterTest() {
        userStorage.deleteUsers();
        userStorage.setId(1);
    }

    @Test
    public void shouldUserControllerPostUserIfAllFieldsAreCorrect() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();

        ResponseEntity<User> response = testRestTemplate.postForEntity("/users", user, User.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(Optional.ofNullable(response.getBody().getId()), Optional.of(1));
        assertEquals(response.getBody().getEmail(), user.getEmail());
        assertEquals(response.getBody().getLogin(), user.getLogin());
        assertEquals(response.getBody().getName(), user.getName());
        assertEquals(response.getBody().getBirthday(), user.getBirthday());
    }

    @Test
    public void shouldUserControllerPostUserIfAllFieldsAreCorrectAndNameIsEmpty() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();

        ResponseEntity<User> response = testRestTemplate.postForEntity("/users", user, User.class);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(Optional.ofNullable(response.getBody().getId()), Optional.of(1));
        assertEquals(response.getBody().getEmail(), user.getEmail());
        assertEquals(response.getBody().getLogin(), user.getLogin());
        assertEquals(response.getBody().getName(), user.getLogin());
        assertEquals(response.getBody().getBirthday(), user.getBirthday());
    }

    @Test
    public void shouldUserControllerUpdateUserIfAllFieldsAreCorrect() {
        User user = User.builder()
                .id(1)
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();

        userStorage.addUser(user);

        final User testUser = userStorage.getUser(1);

        assertEquals(Optional.ofNullable(testUser.getId()), Optional.of(user.getId()));
        assertEquals(testUser.getEmail(), user.getEmail());
        assertEquals(testUser.getLogin(), user.getLogin());
        assertEquals(testUser.getName(), user.getName());
        assertEquals(testUser.getBirthday(), user.getBirthday());

        User user2 = User.builder()
                .id(1)
                .email("Egor@rambler.ru")
                .login("Set")
                .name("Egor")
                .birthday(LocalDate.of(1958, Month.SEPTEMBER, 21))
                .build();

        HttpEntity<User> entity = new HttpEntity<User>(user2);
        ResponseEntity<User> response = testRestTemplate.exchange("/users", HttpMethod.PUT, entity, User.class);
        assertEquals(Objects.requireNonNull(response.getBody()).getId(), userStorage.getUser(1).getId());
        assertEquals(response.getBody().getEmail(), userStorage.getUser(1).getEmail());
        assertEquals(response.getBody().getLogin(), userStorage.getUser(1).getLogin());
        assertEquals(response.getBody().getName(), userStorage.getUser(1).getName());
        assertEquals(response.getBody().getBirthday(), userStorage.getUser(1).getBirthday());
    }

    @Test
    public void shouldUserControllerGetUsersIfAllFieldsAreCorrect() {
        User user = User.builder()
                .id(1)
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();
        User user1 = User.builder()
                .id(2)
                .email("Egor@rambler.ru")
                .login("Set")
                .name("Egor")
                .birthday(LocalDate.of(1958, Month.SEPTEMBER, 21))
                .build();

        userStorage.addUser(user);
        userStorage.addUser(user1);

        final List<User> testUsers = userStorage.getUsers();

        ResponseEntity<List<User>> response = testRestTemplate.exchange(
                "/users",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        final List<User> responseUsers = response.getBody();

        assertEquals(responseUsers.size(), testUsers.size());
        assertEquals(responseUsers.get(0), testUsers.get(0));
        assertEquals(responseUsers.get(1), testUsers.get(1));
    }

    @Test
    public void shouldNotValidationPostUserIfEmailIsBlank() throws Exception {
        User user = User.builder()
                .id(1)
                .email("")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();
        ResponseEntity<User> response = testRestTemplate.postForEntity("/users", user, User.class);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailDogCharIsFirst() {
        User user = User.builder()
                .id(1)
                .email("@Artemrambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();
        ResponseEntity<User> response = testRestTemplate.postForEntity("/users", user, User.class);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailDogCharIsLast() {
        User user = User.builder()
                .id(1)
                .email("Artemrambler.ru@")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();
        ResponseEntity<User> response = testRestTemplate.postForEntity("/users", user, User.class);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailDogCharWithTwoDot() {
        User user = User.builder()
                .id(1)
                .email("Artem@rambler..ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();
        ResponseEntity<User> response = testRestTemplate.postForEntity("/users", user, User.class);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailHasSpaseInMiddle() {
        User user = User.builder()
                .id(1)
                .email("Arte m@rambler..ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();

        ResponseEntity<User> response = testRestTemplate.postForEntity("/users", user, User.class);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldNotValidationAddUserIfUserLoginIsEmpty() {
        User user = User.builder()
                .id(1)
                .email("Artem@rambler.ru")
                .login("")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();

        ResponseEntity<User> response = testRestTemplate.postForEntity("/users", user, User.class);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldNotValidationAddUserIfUserBirthdayBeInFuture() {
        User user = User.builder()
                .id(1)
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(2030, Month.SEPTEMBER, 25))
                .build();

        ResponseEntity<User> response = testRestTemplate.postForEntity("/users", user, User.class);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldNotCustomValidationAddUserIfUserLoginHasSpaceAndEmpty() throws ValidationException {
        User user = User.builder()
                .id(1)
                .email("Artem@rambler.ru")
                .login("Y ")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();


        ResponseEntity<User> response = testRestTemplate.postForEntity("/users", user, User.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
