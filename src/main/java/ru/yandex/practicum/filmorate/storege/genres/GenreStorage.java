package ru.yandex.practicum.filmorate.storege.genres;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.util.List;
import java.util.Map;

public interface GenreStorage {
    List<Genre> getGenres();

    Genre getGenreFromId(Integer genreId);

   Map<Integer, List<Genre>> getFilmsGenres(List<Integer> filmId);
}
