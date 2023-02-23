package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.CustomValidationException;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storege.films.FilmStorage;
import ru.yandex.practicum.filmorate.storege.users.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.validation.Validation.isFilmReleaseDateValidation;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public List<Film> getFilms() {
        return filmStorage.getFilms();
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
        Film film = filmStorage.getFilm(filmId);
        User user = userStorage.getUser(userId);
        film.getLikes().add(user.getId());
    }

    public void deleteFilmLike(Integer filmId, Integer userId) {
        Film film = filmStorage.getFilm(filmId);
        if (userId != null) {
            if (film.getLikes().contains(userId)) {
                film.getLikes().remove(userId);
            } else {
                throw new NotFoundException(String.format("User like id %d not found", userId));
            }
        }
    }

    public List<Film> getPopularFilms(Integer count) {
        LikeComparator likeComparator = new LikeComparator();
        return filmStorage.getFilms()
                .stream()
                .sorted(likeComparator)
                .limit(count)
                .collect(Collectors.toList());
    }

    static class LikeComparator implements Comparator<Film> {
        @Override
        public int compare(Film o1, Film o2) {
            return Integer.compare(o2.getLikes().size(), o1.getLikes().size());
        }
    }
}
