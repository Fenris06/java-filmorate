package ru.yandex.practicum.filmorate.storege.films;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmRate;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getFilms() {
        String sqlQuery = "SELECT f.*, r.rate_name, g.genre_id, g.genre_name FROM films AS f JOIN rate AS r " +
                "ON f.rate_id = r.rate_id LEFT JOIN films_genres AS fg " +
                "ON f.film_id = fg.film_id LEFT JOIN genres AS g " +
                "ON fg.genre_id = g.genre_id";

        return crateFilmsFromBd(sqlQuery);
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
        addGenresToBD(film);
        return getFilm(film.getId());
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
        addGenresToBD(film);
        return getFilm(film.getId());
    }

    @Override
    public Film getFilm(Integer id) {
        String sqlQuery = "SELECT f.*, r.rate_name, g.genre_id, g.genre_name FROM films AS f JOIN rate AS r " +
                "ON f.rate_id = r.rate_id LEFT JOIN films_genres AS fg " +
                "ON f.film_id = fg.film_id LEFT JOIN genres AS g ON fg.genre_id = g.genre_id WHERE f.film_id = " + id;

        List<Film> films = crateFilmsFromBd(sqlQuery);
        if (!films.isEmpty()) {
            return films.get(0);
        } else {
            throw new NotFoundException("film not found");
        }

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
                "LEFT JOIN genres AS g ON fg.genre_id = g.genre_id ORDER BY film_likes DESC LIMIT " + count;

        return crateFilmsFromBd(sqlQuery);
    }
    private List<Film> crateFilmsFromBd(String sqlQuery) {
        Map<Integer, Film> films = new HashMap<>();
        jdbcTemplate.query(sqlQuery, rs -> {
            Integer filmId = rs.getInt("film_id");
            if (!films.containsKey(filmId)) {
                FilmRate mpa = FilmRate.builder().
                        id(rs.getInt("rate_id"))
                        .name(rs.getString("rate_name"))
                        .build();
                Film film = Film.builder().id(filmId)
                        .name(rs.getString("name"))
                        .description(rs.getString("description"))
                        .releaseDate(rs.getDate("release_date").toLocalDate())
                        .duration(rs.getInt("duration"))
                        .mpa(mpa)
                        .build();
                films.put(filmId, film);
            }
            String genre_name = rs.getString("genre_name");
            if (genre_name != null) {
                films.get(filmId).addFilmGenre(Genre.builder()
                        .id(rs.getInt("genre_id"))
                        .name(genre_name).build());
            }
        });
        return new ArrayList<>(films.values());
    }

    private void addGenresToBD(Film film) {
        Integer filmId = film.getId();
        jdbcTemplate.update("DELETE FROM films_genres WHERE film_id = ?", filmId);
        List<Genre> genresList = film.getGenres();
        String addGenresQuery = "MERGE INTO films_genres (film_id, genre_id) VALUES (?,?)";
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
}
