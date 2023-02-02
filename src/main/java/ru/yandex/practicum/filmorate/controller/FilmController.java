package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.validation.Validation;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;


@RestController

@RequestMapping("/films")
public class FilmController {
    private int generationId = 1;
    private final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    @GetMapping
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(Validation.getValidateFilmDate())) {
            log.warn("Film release date is before 28.12.1895 : {}", film);
            throw new ValidationException("Film release date is before 28.12.1895");
        }
        film.setId(generationId++);
        films.put(film.getId(), film);
        log.info("Film is create : {}", film);
        return films.get(film.getId());
    }

    @PutMapping
    public Film uppDateFilm(@Valid @RequestBody Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(Validation.getValidateFilmDate())) {
            log.warn("Film release date is before 28.12.1895 : {}", film);
            throw new ValidationException("Film release date is before 28.12.1895");
        }
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.info("Film is uppDate : {}", film);
            return films.get(film.getId());
        }
        log.warn("Film not uppDate : {}", film);
        throw new ValidationException("Film not update");
    }
}
