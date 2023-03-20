package ru.yandex.practicum.filmorate.databasetest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.model.FilmRate;
import ru.yandex.practicum.filmorate.storege.rates.FilmRateDbStorage;

import java.util.List;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class RateDbStorageGTest {
    private final FilmRateDbStorage filmRateDbStorage;

    @Test
    public void shouldFilmRateDbStorageGetAllRates() {
        List<FilmRate> rates = filmRateDbStorage.getRates();

       assertEquals(rates.size(), 5);
    }

    @Test
    public void shouldFilmRatesDbStorageGetRateFromId() {
        FilmRate rate = filmRateDbStorage.getRateFromId(2);

        assertEquals(rate.getName(), "PG");
    }
}
