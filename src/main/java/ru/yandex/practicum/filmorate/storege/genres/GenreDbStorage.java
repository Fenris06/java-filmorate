package ru.yandex.practicum.filmorate.storege.genres;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class GenreDbStorage implements GenreStorage {
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

    @Override
    public Map<Integer, List<Genre>> getFilmsGenres(List<Integer> filmsId) {
        Map<Integer, List<Genre>> filmsGenres = new HashMap<>();
        SqlParameterSource source = new MapSqlParameterSource("filmsId", filmsId);
        String sqlQuery = "SELECT * FROM films_genres AS fg LEFT JOINT genres AS g ON fg.genres_id = g.genres_id " +
                "WHERE fg.film_id IN (:filmsId)";
        NamedParameterJdbcTemplate template = new NamedParameterJdbcTemplate(jdbcTemplate);
        template.query(sqlQuery, source, rs -> {
            Integer filmId = rs.getInt("film_id");
            String genreName = rs.getString("genre_name");
            if (genreName != null) {
                if (filmsGenres.containsKey(filmId)) {
                    filmsGenres.get(filmId).add(Genre.builder()
                            .id(rs.getInt("genre_id"))
                            .name(genreName).build());
                } else {
                    filmsGenres.put(filmId, List.of(Genre.builder()
                            .id(rs.getInt("genre_id"))
                            .name(genreName).build()));
                }
            }
        });
        return filmsGenres;
    }

    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
        return Genre.builder()
                .id(rs.getInt("genre_id"))
                .name(rs.getString("genre_name"))
                .build();

    }

}

