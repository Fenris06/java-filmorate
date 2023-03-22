package ru.yandex.practicum.filmorate.storege.films;

import ru.yandex.practicum.filmorate.exeption.CustomValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    List<Film> getFilms();

    Film addFilm(Film film);

    Film updateFilm(Film film);

    Film getFilm(Integer id);

    void setFilmLike(Integer filmId, Integer userId);

    void deleteFilmLike(Integer filmId, Integer userId);

    public List<Film> getPopularFilms(Integer count);
}

