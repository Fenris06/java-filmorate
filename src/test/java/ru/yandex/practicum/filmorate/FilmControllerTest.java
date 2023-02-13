package ru.yandex.practicum.filmorate;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import ru.yandex.practicum.filmorate.exeption.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;


import java.time.LocalDate;

import java.util.Arrays;


import static java.time.Month.OCTOBER;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class FilmControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test /*В этом тесте пришлось тестировать все эндпоинты вмести. Если эндпоинты тестировать по отдельности,
           то айдишнк фильма будет произвольным образом менятся и тесты перестанут запускаться все вмести*/
    public void shouldValidationPostFilmIfAllFieldsAreCorrectPutAndGet() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("Artem")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(film)));

        Film film1 = Film.builder()
                .id(1)
                .name("Lev")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/films")
                                .content(objectMapper.writeValueAsString(film1))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(film1)));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(film1))));
    }

    @Test /*Все тесты которые, тестируют аннотации спринга работают корректно*/
    public void shouldNotValidationPostFilmIfFilmNameIsEmpty() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotValidationAddFilmIfDescriptionNotCorrect() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("Artem")
                .description("В центре сюжета — противостояние солдата и робота-терминатора," +
                        " прибывших в 1984 год из постапокалиптического 2029 года. " +
                        "Цель терминатора: убить Сару Коннор — девушку, чей ещё нерождённый" +
                        " сын в возможном будущем выиграет войну человечества с машинами." +
                        " Влюблённый в Сару солдат Кайл Риз пытается помешать терминатору." +
                        " В фильме поднимаются проблемы путешествий во времени, судьбы, " +
                        "создания искусственного интеллекта, поведения людей в экстремальных ситуациях.")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(107)
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotValidationPostFilmIfFilmReleaseDateIsNull() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("Artem")
                .description("Фильм про робота убийцу")
                .releaseDate(null)
                .duration(107)
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotValidationPostFilmIfFilmDurationIsNegative() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("Artem")
                .description("Фильм про робота убийцу")
                .releaseDate(LocalDate.of(1984, OCTOBER, 26))
                .duration(-1)
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/films")
                                .content(objectMapper.writeValueAsString(film))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test /*Здесь исключение кастомной валидация тестируется корректно. В тесте аналогичном тесте для юзера
           исключение не отлавливается*/
    public void shouldNotCustomValidationAddFilmIfReleaseDateIsBeforeValidDate() throws Exception {
        Film film = Film.builder()
                .id(1)
                .name("Artem")
                .description("В центре сюжета.")
                .releaseDate(LocalDate.of(1884, OCTOBER, 26))
                .duration(107)
                .build();

        Exception thrown = assertThrows(Exception.class, () -> {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/films")
                            .content(objectMapper.writeValueAsString(film))
                            .contentType(MediaType.APPLICATION_JSON)
            );
        });

        assertEquals(thrown.getCause().getClass(), ValidationException.class);
    }
}





