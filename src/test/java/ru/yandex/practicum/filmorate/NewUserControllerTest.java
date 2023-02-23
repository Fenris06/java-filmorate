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
