package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;


import java.time.LocalDate;

import static java.time.Month.*;
import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.validation.Validation.isFilmValidation;
import static ru.yandex.practicum.filmorate.validation.Validation.isUserValidation;

public class ValidationTest {

    @Test
    public void shouldValidationReturnTrueIfAllFilmVariablesAreRight() {
        Film film = Film.builder()
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        assertTrue(isFilmValidation(film), "Validation not true");
    }

    @Test
    public void shouldValidationReturnFalseIfFilmDescriptionMoreThat200Symbols() {
        Film film = Film.builder()
                .name("")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        assertFalse(isFilmValidation(film), "Validation not false");
    }

    @Test
    public void shouldValidationReturnFalseIfFilmNameIsEmpty() {
        Film film = Film.builder()
                .name("Terminator")
                .description("В центре сюжета — противостояние солдата и робота-терминатора," +
                        " прибывших в 1984 год из постапокалиптического 2029 года. " +
                        "Цель терминатора: убить Сару Коннор — девушку, чей ещё нерождённый" +
                        " сын в возможном будущем выиграет войну человечества с машинами." +
                        " Влюблённый в Сару солдат Кайл Риз пытается помешать терминатору." +
                        " В фильме поднимаются проблемы путешествий во времени, судьбы, " +
                        "создания искусственного интеллекта, поведения людей в экстремальных ситуациях.")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        assertFalse(isFilmValidation(film), "Validation not false");
    }

    @Test
    public void shouldValidationReturnFalseIfFilmReleaseDateIsAfterValidateFilmDate() {
        Film film = Film.builder()
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1895, DECEMBER, 27))
                .duration(107)
                .build();
        assertFalse(isFilmValidation(film), "Validation not false");
    }

    @Test
    public void shouldValidationReturnFalseIfFilmDurationIsLessThanZero() {
        Film film = Film.builder()
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(-1)
                .build();

        assertFalse(isFilmValidation(film), "Validation not true");
    }

    @Test
    public void shouldValidationReturnTrueIfAllUserVariablesAreRight() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        assertTrue(isUserValidation(user));
    }

    @Test
    public void shouldValidationReturnFalseIfUserEmailIsBlankOrMissingCharacter() {
        User user = User.builder()
                .email("")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        assertFalse(isUserValidation(user));

        User user1 = User.builder()
                .email("Artemrambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        assertFalse(isUserValidation(user1));
    }

    @Test
    public void shouldValidationReturnFalseIfUserLoginIsBlankOrHaveSpace() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        assertFalse(isUserValidation(user));

        User user1 = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris is")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        assertFalse(isUserValidation(user1));
    }

    @Test
    public void shouldValidationReturnFalseIfUserLoginIsAfterValidationBirthdayDate() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(2024, SEPTEMBER, 25))
                .build();

        assertFalse(isUserValidation(user));
    }
}
