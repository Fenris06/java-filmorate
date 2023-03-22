package ru.yandex.practicum.filmorate.storege.rates;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmRate;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class FilmRateDbStorage implements FilmRateStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<FilmRate> getRates() {
        String sqlQuery = "SELECT * FROM rate";
        return jdbcTemplate.query(sqlQuery, this::mapRowToFilmRate);
    }

    @Override
    public FilmRate getRateFromId(Integer rateId) {
        String sqlQuery = "SELECT * FROM rate WHERE rate_id = ?";
        return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilmRate, rateId);
    }

    private FilmRate mapRowToFilmRate(ResultSet rs, int rowNum) throws SQLException {
        return FilmRate.builder()
                .id(rs.getInt("rate_id"))
                .name(rs.getString("rate_name"))
                .build();
    }
}
