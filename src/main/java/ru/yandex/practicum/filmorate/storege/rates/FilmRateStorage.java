package ru.yandex.practicum.filmorate.storege.rates;

import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.FilmRate;

import java.util.List;

public interface FilmRateStorage {
    List<FilmRate> getRates();

    FilmRate getRateFromId(Integer rateId);
}
