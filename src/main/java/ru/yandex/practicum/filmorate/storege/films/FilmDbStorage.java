package ru.yandex.practicum.filmorate.storege.films;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storege.genres.GenreDbStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private final GenreDbStorage genreDbStorage;

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT f.*, r.rate_name, g.genre_id, g.genre_name FROM films AS f JOIN rate AS r " +
                "ON f.rate_id = r.rate_id LEFT JOIN films_genres AS fg " +
                "ON f.film_id = fg.film_id LEFT JOIN genres AS g " +
                "ON fg.genre_id = g.genre_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm);
    }

    @Override
    public Film addFilm(Film film) {
        String sqlQuery = "INSERT INTO films (name, description, release_date, duration, rate_id)" +
                "values(?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"film_id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            film.setId((Integer) keyHolder.getKey());
        }
        replaceFilmGernes(film);
        return film;
    }

    @Override
    public Film updateFilm(Film film) {
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, duration = ?, rate_id = ? " +
                "WHERE film_id = ?";
        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                Date.valueOf(film.getReleaseDate()),
                film.getDuration(),
                film.getMpa().getId(),
                film.getId());
        replaceFilmGernes(film);
        return film;
    }

    @Override
    public Film getFilm(Integer id) {
        String sqlQuery = "SELECT f.*, r.rate_name, g.genre_id, g.genre_name FROM films AS f JOIN rate AS r " +
                "ON f.rate_id = r.rate_id LEFT JOIN films_genres AS fg " +
                "ON f.film_id = fg.film_id LEFT JOIN genres AS g ON fg.genre_id = g.genre_id WHERE f.film_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
    }

    @Override
    public void setFilmLike(Integer filmId, Integer userId) {
        String sqlQuery = "INSERT INTO likes (film_id, user_id) VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public void deleteFilmLike(Integer filmId, Integer userId) {
        String sqlQuery = "DELETE FROM likes WHERE film_id = ? AND user_id = ?";
        jdbcTemplate.update(sqlQuery, filmId, userId);
    }

    @Override
    public List<Film> getPopularFilms(Integer count) {
        String sqlQuery = "SELECT f.*, r.rate_name, g.genre_id, g.genre_name FROM films AS f " +
                "LEFT JOIN (SELECT film_id, COUNT(user_id) AS film_likes FROM likes GROUP BY film_id) AS l " +
                "ON f.film_id = l.film_id LEFT JOIN rate AS r " +
                "ON f.rate_id = r.rate_id LEFT JOIN films_genres AS fg ON f.film_id = fg.film_id " +
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id ORDER BY film_likes DESC LIMIT ?";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilm, count);
    }

    private Film mapRowToFilm(ResultSet rs, int rowNum) throws SQLException {

        FilmRate mpa = FilmRate.builder().
                id(rs.getInt("rate_id"))
                .name(rs.getString("rate_name"))
                .build();
        Integer filmId = rs.getInt(rs.getInt("film_id"));
        Film film = Film.builder().id(filmId)
                .name(rs.getString("name"))
                .description(rs.getString("description"))
                .releaseDate(rs.getDate("release_date").toLocalDate())
                .duration(rs.getInt("duration"))
                .mpa(mpa)
                .build();
        List<Genre> filmGenres = new ArrayList<>();
        String genre_name = rs.getString("genre_name");
        if (genre_name != null) {
            filmGenres.add(Genre.builder()
                    .id(rs.getInt("genre_id"))
                    .name(genre_name).build());
        }
        for (Genre genre : filmGenres) {
            film.addFilmGenre(genre);
        }
        return film;
    }

    private void replaceFilmGernes(Film film) {
        Integer filmId = film.getId();
        jdbcTemplate.update("DELETE FROM films_genres WHERE film_id = ?", filmId);
        List<Genre> genresList = film.getGenres();
        String addGenresQuery = "MERGE INTO films_genres (film_id,genre_id) VALUES (?,?)";
        jdbcTemplate.batchUpdate(addGenresQuery, new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setLong(1, filmId);
                ps.setLong(2, genresList.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genresList.size();
            }
        });
    }

//    private void addFIlmGenre(Integer filmId, Set<Genre> genres) {
//        String sqlQuery = "INSERT INTO films_genres (genre_id, film_id) VALUES (?, ?)";
//        for (Genre genre : genres) {
//            Integer genreId = genre.getId();
//            jdbcTemplate.update(sqlQuery, genreId, filmId);
//        }
//    }
//
//    private List<Genre> getFilmGenres(Integer id) {
//        String sqlQuery = "SELECT genre_id, genre_name FROM films_genres AS f " +
//                "LEFT JOIN genres AS g ON f.genre_id = g.genre_id WHERE f.film_id = ?";
//        return jdbcTemplate.query(sqlQuery,new BeanPropertyRowMapper<>(Genre.class), id);
//    }
//    private Genre mapRowToGenre(ResultSet rs, int rowNum) throws SQLException {
//        return Genre.builder()
//                .id(rs.getInt("genre_id"))
//                .name(rs.getString("genre_name"))
//                .build();
//    }
//
//    private List<Genre> getFilmGenres(int id) {
//String sqlQuery = "SELECT f.film_id, genre_name FROM films_genres f" +
//        " LEFT JOIN (SELECT * FROM genres) g ON f.genre_id = g.genre_id " +
//        "WHERE film_id = ?";
//        return jdbcTemplate.query(sqlQuery, new BeanPropertyRowMapper<>(Genre.class), id);
//    }
}
