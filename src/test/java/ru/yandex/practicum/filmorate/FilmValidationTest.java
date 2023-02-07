package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static java.time.Month.DECEMBER;
import static java.time.Month.OCTOBER;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static ru.yandex.practicum.filmorate.validation.Validation.isFilmReleaseDateValidation;

public class FilmValidationTest {
    Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Test
    public void shouldValidationAddFilmIfAllFieldsAreCorrect() {
        Film film = Film.builder()
                .name("Artem")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(violations.size(), 0);
    }

    @Test
    public void shouldNotValidationAddFilmIfFilmNameIsBlank() {
        Film film = Film.builder()
                .name(" ")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(violations.size(), 1);
    }

    @Test
    public void shouldNotValidationAddFilmIfFilmDescriptionIsOver200Characters() {
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

        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(violations.size(), 1);
    }

    @Test
    public void shouldNotValidationAddFilmIfFilmReleaseDateIsNull() {
        Film film = Film.builder()
                .name("Artem")
                .description("Фильм про робота убийцу")
                .duration(107)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(violations.size(), 1);
    }

    @Test
    public void shouldNotCustomValidationAddFilmIfFilmReleaseDateIsBeforeValidDate() {
        Film film = Film.builder()
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1895, DECEMBER, 27))
                .duration(107)
                .build();

        assertFalse(isFilmReleaseDateValidation(film), "Validation not false");
    }

    @Test
    public void shouldNotValidationAddFilmIfFilmDurationIsNegativeNumber() {
        Film film = Film.builder()
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(-1)
                .build();
        Set<ConstraintViolation<Film>> violations = validator.validate(film);

        assertEquals(violations.size(), 1);
    }
    }



