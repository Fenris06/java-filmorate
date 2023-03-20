package ru.yandex.practicum.filmorate.databasetest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storege.genres.GenreDbStorage;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class GenresDbStorageTest {
    private final GenreDbStorage genreDbStorage;

    @Test
    public void shouldGenresDbStorageGetAllGenres() {
        List<Genre> genres = genreDbStorage.getGenres();
        assertEquals(genres.size(), 6);
    }

    @Test
    public void shouldGenresDbStorageGetGenreFromId() {
        Genre testGenre = genreDbStorage.getGenreFromId(2);

        assertEquals(testGenre.getName(), "Драма");
    }


}
