package ru.yandex.practicum.filmorate;

import org.junit.After;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import ru.yandex.practicum.filmorate.model.Film;

import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storege.films.FilmStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static java.time.Month.*;
import static java.util.Calendar.JULY;
import static java.util.Calendar.OCTOBER;
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FilmStorageTest {
    @Autowired
   private FilmStorage filmStorage;

    @Test
    public void shouldFilmStorageAddFilmIfAllFieldsAreCorrect() {
        Film film = Film.builder()
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        final Film testFilm = filmStorage.addFilm(film);

        assertEquals(testFilm.getName(), film.getName());
        assertEquals(testFilm.getDescription(), film.getDescription());
        assertEquals(testFilm.getReleaseDate(), film.getReleaseDate());
        assertEquals(testFilm.getDuration(), film.getDuration());
    }

    @Test
    public void shouldFilmStorageUpdateFilmIfAllFieldsAreCorrect() {
        Film film = Film.builder()
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        final Film updateFilm = filmStorage.addFilm(film);

        updateFilm.setName("RoboCop");
        updateFilm.setDescription("Фильм про робота полицейского");
        updateFilm.setReleaseDate(LocalDate.of(1987, JULY, 17));
        updateFilm.setDuration(102);

        final Film testFilm = filmStorage.updateFilm(updateFilm);

        assertEquals(testFilm.getName(), updateFilm.getName());
        assertEquals(testFilm.getDescription(), updateFilm.getDescription());
        assertEquals(testFilm.getReleaseDate(), updateFilm.getReleaseDate());
        assertEquals(testFilm.getDuration(), updateFilm.getDuration());
    }

    @Test
    public void shouldFilmStorageGetFilmsIfAllFieldsAreCorrect() {
        Film film = Film.builder()
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        Film film1 = Film.builder()
                .name("RoboCop")
                .description("Фильм про робота полицейского")
                .releaseDate(LocalDate.of(1987, JULY, 17))
                .duration(102)
                .build();

        filmStorage.addFilm(film);
        filmStorage.addFilm(film1);

        final List<Film> films = filmStorage.getFilms();
        assertEquals(films.size(), 2);
    }

    @Test
    public void shouldFilmStorageGetFilmFromIdIfAllFieldsAreCorrect() {
        Film film = Film.builder()
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        filmStorage.addFilm(film);

        final Film testFilm = filmStorage.getFilm(film.getId());

        assertEquals(testFilm.getName(), film.getName());
        assertEquals(testFilm.getDescription(), film.getDescription());
        assertEquals(testFilm.getReleaseDate(), film.getReleaseDate());
        assertEquals(testFilm.getDuration(), film.getDuration());
    }
}
