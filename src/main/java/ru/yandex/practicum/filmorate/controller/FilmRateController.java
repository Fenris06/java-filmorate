package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.FilmRate;
import ru.yandex.practicum.filmorate.service.FilmRateService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
@RequiredArgsConstructor
public class FilmRateController {
    private final FilmRateService filmRateService;

    @GetMapping
    public List<FilmRate> getFilmRates() {
        return filmRateService.getRates();
    }

    @GetMapping("/{id}")
    public FilmRate getFilmRateFromId(@PathVariable("id") Integer rateId) {
        return filmRateService.getRateFromId(rateId);
    }
}
