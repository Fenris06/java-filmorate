package ru.yandex.practicum.filmorate.storege.films;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.exeption.CustomValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {


    private Integer generationId = 1;
    private final Map<Integer, Film> films = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(InMemoryFilmStorage.class);

    @Override
    public List<Film> getFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film addFilm(Film film) {
        film.setId(generationId++);
        films.put(film.getId(), film);
        log.info("Film is create : {}", film);
        return films.get(film.getId());
    }

    @Override
    public Film updateFilm(Film film) {
        if (films.containsKey(film.getId())) {
            films.replace(film.getId(), film);
            log.info("Film is uppDate : {}", film);
            return films.get(film.getId());
        }
        log.warn("Film not uppDate : {}", film);
        throw new NotFoundException(String.format("Film by id %d not update", film.getId()));
    }

    @Override
    public void deleteFilms() {
        films.clear();
    }

    @Override
    public Film getFilm(Integer id) {
        if (id == null) {
            throw new NullPointerException("Film id not integer");
        }
        if (films.get(id) != null) {
            log.info("Film is return : {}", films.get(id));
            return films.get(id);
        } else {
            log.warn("Film by id not found : {}", id);
            throw new NotFoundException(String.format("Film by id %d not found", id));
        }
    }

    @Override
    public void setId(Integer id) {
        this.generationId = id;
    }


}



