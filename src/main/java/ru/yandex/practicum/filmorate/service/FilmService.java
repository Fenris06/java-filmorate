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

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validation.Validation.isFilmReleaseDateValidation;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final GenreDbStorage genreDbStorage;

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
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        if (!isFilmReleaseDateValidation(film)) {
            log.warn("Film release date is before 28.12.1895 : {}", film);
            throw new CustomValidationException("Film release date is before 28.12.1895");
        }
        return filmStorage.updateFilm(film);
    }

    public Film getFilm(Integer id) {
        return filmStorage.getFilm(id);
    }

    public void setFilmLike(Integer filmId, Integer userId) {
        filmStorage.setFilmLike(filmId, userId);
    }

    public void deleteFilmLike(Integer filmId, Integer userId) {
        if (filmId >= 1 && userId >= 1) {
            filmStorage.deleteFilmLike(filmId, userId);
        } else {
            throw new NotFoundException("id must be positive");
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        return filmStorage.getPopularFilms(count);
    }

}
