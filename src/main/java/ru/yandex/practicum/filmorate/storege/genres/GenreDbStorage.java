package ru.yandex.practicum.filmorate.storege.genres;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage{
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Genre> getGenres() {
        String sqlQuery = "SELECT * FROM genres";
       return jdbcTemplate.query(sqlQuery, this::mapRowToGenre);
    }

    @Override
    public Genre getGenreFromId(Integer genreId) {
        String sqlQuery = "SELECT * FROM genres WHERE genre_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToGenre, genreId);
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return  Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();

    }
}
