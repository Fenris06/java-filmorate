package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.CustomValidationException;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;


import javax.validation.Valid;
import java.util.List;


import static ru.yandex.practicum.filmorate.validation.Validation.isFilmReleaseDateValidation;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {
    private final FilmService filmService;

    @Autowired
    public FilmController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping
    public List<Film> getFilms() {
        return filmService.getFilms();
    }

    @PostMapping
    public Film createFilm(@Valid @RequestBody Film film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public Film uppDateFilm(@Valid @RequestBody Film film)  {
        return filmService.updateFilm(film);
    }

    @GetMapping("/{id}")
    public Film getFIlmById(@PathVariable("id") Integer id) {
        return filmService.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public Integer setFilmLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.setFilmLike(id, userId);
        return id;
    }

    @DeleteMapping("/{id}/like/{userId}")
    public Integer deleteFilmLike(@PathVariable("id") Integer id, @PathVariable("userId") Integer userId) {
        filmService.deleteFilmLike(id, userId);
        return id;
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilm(@RequestParam(defaultValue ="10", required = false) Integer count) {
        if(count <= 0) {
            throw new NotFoundException(String.format("Number count %d must be greater than zero", count));
        }
        return filmService.getPopularFilms(count);
    }

}
