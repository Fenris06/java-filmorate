package ru.yandex.practicum.filmorate.databasetest;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.jdbc.Sql;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storege.films.FilmDbStorage;
import ru.yandex.practicum.filmorate.storege.users.UserDbStorage;

import java.time.LocalDate;
import java.util.List;

import static java.util.Calendar.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql(scripts = "classpath:data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;

    @Test
    public void shouldFilmDbStorageAddFilm() {
        FilmRate filmRate = FilmRate.builder().id(1).build();
        Genre genre = Genre.builder().id(1).build();
        Film film = Film.builder()
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .mpa(filmRate)
                .build();


        Film testFilm = filmDbStorage.addFilm(film);
        assertEquals(testFilm.getName(), film.getName());
        assertEquals(testFilm.getMpa().getName(), "G");

    }

    @Test
    public void shouldFilmDbStorageUpdateFilm() {
        FilmRate filmRate = FilmRate.builder().id(1).build();

        Film film = Film.builder()
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .mpa(filmRate)
                .build();


        Film updateFilm = filmDbStorage.addFilm(film);
        FilmRate updateRate = FilmRate.builder().id(2).build();

        updateFilm.setName("RoboCop");
        updateFilm.setDescription("Фильм про робота полицейского");
        updateFilm.setReleaseDate(LocalDate.of(1987, JULY, 17));
        updateFilm.setDuration(102);
        updateFilm.setMpa(updateRate);


        Film testFilm = filmDbStorage.updateFilm(updateFilm);

        assertEquals(testFilm.getName(), updateFilm.getName());
        assertEquals(testFilm.getMpa().getName(), "PG");

    }

    @Test
    public void shouldFilmDbStorageGetFilmFromId() {
        FilmRate filmRate = FilmRate.builder().id(1).build();
        Genre genre = Genre.builder().id(1).build();
        Film film = Film.builder()
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .mpa(filmRate)
                .build();


        Film getFilm = filmDbStorage.addFilm(film);
        Film testFilm = filmDbStorage.getFilm(getFilm.getId());

        assertEquals(testFilm.getName(), film.getName());
        assertEquals(testFilm.getMpa().getName(), "G");

    }

    @Test
    public void shouldFilmDbStorageAddLikeToFilmAndGetPopularFilm() {
        User user = User.builder()
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();

        FilmRate filmRate = FilmRate.builder().id(3).build();

        Film film = Film.builder()
                .name("RoboCop")
                .description("Фильм про робота полицейского")
                .releaseDate(LocalDate.of(1987, JULY, 17))
                .duration(102)
                .mpa(filmRate)
                .build();

        Film bestTest = filmDbStorage.addFilm(film);
        User likeUser = userDbStorage.addUser(user);
        filmDbStorage.setFilmLike(bestTest.getId(), likeUser.getId());

        List<Film> films = filmDbStorage.getPopularFilms(10);
        assertEquals(films.get(0).getName(), bestTest.getName());

    }

    @Test
    public void shouldFilmDbStorageGetAllFilms() {
        List<Film> testFilms = filmDbStorage.getFilms();
        assertEquals(testFilms.size(), 1);
    }
}
