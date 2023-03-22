package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.CustomValidationException;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storege.films.FilmStorage;
import ru.yandex.practicum.filmorate.storege.genres.GenreDbStorage;
import ru.yandex.practicum.filmorate.storege.users.UserStorage;

import java.util.*;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validation.Validation.isFilmReleaseDateValidation;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreDbStorage genreDbStorage;

    private final UserService userService;

    public List<Film> getFilms() {
        List<Film> films = filmStorage.getFilms();
        List<Integer> filmIds = new ArrayList<>();
        if (!films.isEmpty()) {
            for (Film film : films) {
                filmIds.add(film.getId());
            }
            Map<Integer, List<Genre>> filmGenres = genreDbStorage.getFilmsGenres(filmIds);
            for (Film film : films) {
                if (filmGenres.containsKey(film.getId())) {
                    film.addFilmGenre(filmGenres.get(film.getId()));
                }
            }
        }
        return films;
    }

    public Film addFilm(Film film) {
        if (!isFilmReleaseDateValidation(film)) {
            log.warn("Film release date is before 28.12.1895 : {}", film);
            throw new CustomValidationException("Film release date is before 28.12.1895");
        }

        return getFilm(filmStorage.addFilm(film).getId());
    }

    public Film updateFilm(Film film) {
        if (!isFilmReleaseDateValidation(film)) {
            log.warn("Film release date is before 28.12.1895 : {}", film);
            throw new CustomValidationException("Film release date is before 28.12.1895");
        }
        if (getFilm(film.getId()) != null) {
            return getFilm(filmStorage.updateFilm(film).getId());
        } else {
            throw new NotFoundException("Film not found");
        }
    }
    public Film getFilm(Integer id) {
        Film film = filmStorage.getFilm(id);
        List<Integer> filmIds = new ArrayList<>();
        filmIds.add(film.getId());
        Map<Integer, List<Genre>> filmGenres = genreDbStorage.getFilmsGenres(filmIds);
        List<Genre> newGenre = new ArrayList<>();
        for (List<Genre> genres : filmGenres.values()) {
            film.addFilmGenre(genres);
        }

       return film;
    }

    public void setFilmLike(Integer filmId, Integer userId) {
        if (getFilm(filmId) != null && userService.getUser(userId) != null) {
            filmStorage.setFilmLike(filmId, userId);
        } else {
            throw new NotFoundException("user or film not found");
        }
    }
    public void deleteFilmLike(Integer filmId, Integer userId) {
        if (getFilm(filmId) != null && userService.getUser(userId) != null) {
            filmStorage.deleteFilmLike(filmId, userId);
        } else {
            throw new NotFoundException("user or film not found");
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        List<Film> films = filmStorage.getPopularFilms(count);
        List<Integer> filmIds = new ArrayList<>();
        if (!films.isEmpty()) {
            for (Film film : films) {
                filmIds.add(film.getId());
            }
            Map<Integer, List<Genre>> filmGenres = genreDbStorage.getFilmsGenres(filmIds);
            for (Film film : films) {
                if (filmGenres.containsKey(film.getId())) {
                    film.addFilmGenre(filmGenres.get(film.getId()));
                }
            }
        }
        return films;
    }

}
