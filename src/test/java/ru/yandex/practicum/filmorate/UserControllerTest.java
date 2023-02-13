package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.time.LocalDate;
import java.time.Month;
import java.util.Arrays;

import static java.util.Calendar.SEPTEMBER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test /*Здесь аналогично тестам фильмов. Все тестирование с правильными полями пришлось засунуть в один тест.
    Плюс отдельно не получилось создать класс для тестирования эдпоинтов юзера*/
    public void shouldValidationPostUserIfAllFieldsAreCorrectPutAndGetAndSetNameIsLoginEmpty() throws Exception {
        User user = User.builder()
                .id(1)
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));

        User user1 = User.builder()
                .id(1)
                .email("Artem@rambler.ru")
                .login("Artem")
                .name("")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();

        User user2 = User.builder()
                .id(1)
                .email("Artem@rambler.ru")
                .login("Artem")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/users")
                                .content(objectMapper.writeValueAsString(user1))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user2)));


        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(user2))));
    }

    @Test /*Все тесты которые, тестируют аннотации спринга работают корректно*/
    public void shouldNotValidationPostUserIfEmailIsBlank() throws Exception {
        User user = User.builder()
                .id(1)
                .email("")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailDogCharIsFirst() throws Exception {
        User user = User.builder()
                .id(1)
                .email("@Artemrambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailDogCharIsLast() throws Exception {
        User user = User.builder()
                .id(1)
                .email("Artemrambler.ru@")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailDogCharWithTwoDot() throws Exception {
        User user = User.builder()
                .id(1)
                .email("Artem@rambler..ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotValidationAddUserIfUserEmailHasSpaseInMiddle() throws Exception {
        User user = User.builder()
                .id(1)
                .email("Arte m@rambler..ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotValidationAddUserIfUserLoginIsEmpty() throws Exception {
        User user = User.builder()
                .id(1)
                .email("Artem@rambler.ru")
                .login("")
                .name("Artem")
                .birthday(LocalDate.of(1988, Month.SEPTEMBER, 25))
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    @Test
    public void shouldNotValidationAddUserIfUserBirthdayBeInFuture() throws Exception {
        User user = User.builder()
                .id(1)
                .email("Artem@rambler.ru")
                .login("Fenris")
                .name("Artem")
                .birthday(LocalDate.of(2030, Month.SEPTEMBER, 25))
                .build();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isBadRequest());
    }

    /*Этот тест сделан по аналогии с тестом для фильма.Его запуск приводит к ошибки*/
    @Test
    public void shouldNotCustomValidationAddUserIfUserLoginHasSpaceAndEmpty() throws Exception{
        User user = User.builder()
                .id(1)
                .email("Artem@rambler.ru")
                .login("Y ")
                .name("Artem")
                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
                .build();



        Exception thrown = assertThrows(Exception.class, () -> {
            mockMvc.perform(
                    MockMvcRequestBuilders.post("/users")
                            .content(objectMapper.writeValueAsString(user))
                            .contentType(MediaType.APPLICATION_JSON)
            );
        });
        assertEquals(thrown.getCause().getClass(), ValidationException.class);
    }
}


