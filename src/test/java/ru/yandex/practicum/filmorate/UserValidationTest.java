package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.validation.Validation.*;

public class UserValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void shouldValidationAddUserIfAllFieldsAreCorrect() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(violations.size(), 0);
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailIsNull() {
        User user = User.builder()
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(violations.size(), 1);
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailIsBlank() {
        User user = User.builder()
                .email("")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(violations.size(), 1);
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailIsBlankAndWithSpace() {
        User user = User.builder()
                .email(" ")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(violations.size(), 2);
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailWithoutDogChar() {
        User user = User.builder()
                .email("Artemrambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(violations.size(), 1);
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailDogCharIsFirst() {
        User user = User.builder()
                .email("@Artemrambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(violations.size(), 1);
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailDogCharIsLast() {
        User user = User.builder()
                .email("Artemrambler.ru@")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(violations.size(), 1);
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailDogCharWithTwoDot() {
        User user = User.builder()
                .email("Artem@rambler..ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(violations.size(), 1);
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailHasSpaseInMiddle(){
        User user = User.builder()
                .email("Arte m@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(violations.size(), 1);
    }

    @Test
    public void shouldCustomValidationReturnFalseIfUserLoginIsEmpty() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        assertFalse(isLoginUserValidation(user.getLogin()), "Validation not false");
    }

    @Test
    public void shouldCustomValidationReturnTrueIfUserLoginHasSpase() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login(" ")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        assertTrue(isLoginUserValidation(user.getLogin()), "Validation not true");
    }

    @Test
    public void shouldCustomValidationReturnTrueIfUserLoginHasSpaseInWithWordsInFirst() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login(" Login")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        assertTrue(isLoginUserValidation(user.getLogin()), "Validation not true");
    }

    @Test
    public void shouldCustomValidationReturnTrueIfUserLoginHasSpaseInWithWordsInMiddle() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Lo gin")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        assertTrue(isLoginUserValidation(user.getLogin()), "Validation not true");
    }
    @Test
    public void shouldCustomValidationReturnTrueIfUserLoginHasSpaseInWithWordsInEnd() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Login ")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        assertTrue(isLoginUserValidation(user.getLogin()), "Validation not true");
    }

    @Test
    public void shouldCustomValidationSetNameIfUserNameIsNull() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Login")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        setUserLoginValidation(user);

        assertEquals(user.getName(), user.getLogin(), "Words not same");
    }

    @Test
    public void shouldCustomValidationSetNameIfUserNameIsEmpty() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Login")
                .name("")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        setUserLoginValidation(user);

        assertEquals(user.getName(), user.getLogin(), "Words not same");
    }

    @Test
    public void shouldNotValidationAddUserIfUserBirthdayBeInFuture() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(2030, SEPTEMBER, 25))
                .build();
        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertEquals(violations.size(), 1);
    }
    }

