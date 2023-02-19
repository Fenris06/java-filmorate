package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storege.films.FilmStorage;
import ru.yandex.practicum.filmorate.storege.users.UserStorage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public List<Film> getFilms() {
        return filmStorage.getFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
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
        List<Film> films = new ArrayList<>(filmStorage.getFilms());
        films.sort(likeComparator);
        if (count == 10) {
            if (films.size() <= 10) {
                return films;
            } else {
                return films.subList(0, 9);
            }
        } else {
            if (count <= films.size()) {
                return films.subList(0, count);
            } else {
                return films;
            }
        }
    }

    static class LikeComparator implements Comparator<Film> {
        @Override
        public int compare(Film o1, Film o2) {
            return Integer.compare(o2.getLikes().size(), o1.getLikes().size());
        }
    }
}
