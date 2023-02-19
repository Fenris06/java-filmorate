package ru.yandex.practicum.filmorate;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NewFilmControllerTest {
    @Autowired
    private TestRestTemplate testRestTemplate;
    @Autowired
    private FilmStorage filmStorage;
    @Autowired
    private FilmService filmService;

    @After
    public void afterTest() {
        filmStorage.deleteFilms();
        filmStorage.setId(1);
    }

    @Test
    public void shouldValidationPostFilmIfAllFieldsAreCorrect() {
        Film film = Film.builder()
                .id(1)
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();
        ResponseEntity<Film> response = testRestTemplate.postForEntity("/films", film, Film.class);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(Optional.ofNullable(response.getBody().getId()), Optional.of(1));
        assertEquals(response.getBody().getName(), film.getName());
        assertEquals(response.getBody().getDescription(), film.getDescription());
        assertEquals(response.getBody().getReleaseDate(), film.getReleaseDate());
        assertEquals(response.getBody().getDuration(), film.getDuration());
    }

    @Test
    public void shouldValidationPutFilmIfAllFieldsAreCorrect() {
        Film film = Film.builder()
                .id(1)
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        filmStorage.addFilm(film);

        final Film testFilm = filmStorage.getFilm(1);
        assertEquals(testFilm, film);

        Film film1 = Film.builder()
                .id(1)
                .name("RoboCop")
                .description("Фильм про робота полицейского")
                .releaseDate(LocalDate.of(1987, JULY, 17))
                .duration(102)
                .build();


        HttpEntity<Film> entity = new HttpEntity<>(film1);
        ResponseEntity<Film> response = testRestTemplate.exchange("/films", HttpMethod.PUT, entity, Film.class);
        final Film updateFilm = filmStorage.getFilm(1);

        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(Optional.ofNullable(response.getBody().getId()), Optional.of(testFilm.getId()));
        assertEquals(response.getBody().getName(), updateFilm.getName());
        assertEquals(response.getBody().getDescription(), updateFilm.getDescription());
        assertEquals(response.getBody().getReleaseDate(), updateFilm.getReleaseDate());
        assertEquals(response.getBody().getDuration(), updateFilm.getDuration());
    }

    @Test
    public void shouldUserControllerGetFilmsIfAllFieldsAreCorrect() {
        Film film = Film.builder()
                .id(1)
                .name("Terminator")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        Film film1 = Film.builder()
                .id(2)
                .name("RoboCop")
                .description("Фильм про робота полицейского")
                .releaseDate(LocalDate.of(1987, JULY, 17))
                .duration(102)
                .build();

        filmStorage.addFilm(film);
        filmStorage.addFilm(film1);

        final List<Film> testFilms = filmStorage.getFilms();

        ResponseEntity<List<Film>> response = testRestTemplate.exchange(
                "/films",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<>() {
                });
        final List<Film> responseFilms = response.getBody();

        assertEquals(responseFilms.size(), testFilms.size());
        assertEquals(responseFilms.get(0), testFilms.get(0));
        assertEquals(responseFilms.get(1), testFilms.get(1));
    }

    @Test
    public void shouldNotValidationPostFilmIfFilmNameIsEmpty() {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        ResponseEntity<Film> response = testRestTemplate.postForEntity("/films", film, Film.class);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldNotValidationAddFilmIfDescriptionNotCorrect() {
        Film film = Film.builder()
                .id(1)
                .name("Artem")
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

        ResponseEntity<Film> response = testRestTemplate.postForEntity("/films", film, Film.class);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldNotValidationPostFilmIfFilmReleaseDateIsNull() {
        Film film = Film.builder()
                .id(1)
                .name("Artem")
                .description("Фильм про робота убийцу")
                .releaseDate(null)
                .duration(107)
                .build();

        ResponseEntity<Film> response = testRestTemplate.postForEntity("/films", film, Film.class);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldNotValidationPostFilmIfFilmDurationIsNegative() {
        Film film = Film.builder()
                .id(1)
                .name("Artem")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(-1)
                .build();

        ResponseEntity<Film> response = testRestTemplate.postForEntity("/films", film, Film.class);

        assertEquals(response.getStatusCode(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Test
    public void shouldNotCustomValidationAddFilmIfReleaseDateIsBeforeValidDate() {
        Film film = Film.builder()
                .id(1)
                .name("Artem")
                .description("В центре сюжета.")
                .releaseDate(LocalDate.of(1884, OCTOBER, 26))
                .duration(107)
                .build();

        ResponseEntity<Film> response = testRestTemplate.postForEntity("/films", film, Film.class);

        assertEquals(response.getStatusCode(), HttpStatus.BAD_REQUEST);
    }
}
