package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.FilmRate;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.storege.rates.FilmRateStorage;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmRateService {
    private final FilmRateStorage filmRateStorage;

    public List<FilmRate> getRates() {
        return filmRateStorage.getRates();
    }

    public FilmRate getRateFromId(Integer rateId) {
       try {
           return filmRateStorage.getRateFromId(rateId);
       } catch (Exception e) {
           throw new NotFoundException("rate not found");
       }
    }
}
