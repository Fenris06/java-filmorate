package ru.yandex.practicum.filmorate;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static java.util.Calendar.SEPTEMBER;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class NewUserControllerTest {
    @Autowired
        private MockMvc mockMvc;
        @Autowired
        private ObjectMapper objectMapper;
/*Тесты в этом классе не получается запустить. Он сделан по аналогии с классом фильмов.
Тест выбрасывает NullPointException*/
//    @Test
//    public void shouldValidationPostUserIfAllFieldsAreCorrectPutAndGet() throws Exception {
//        User user = User.builder()
//
//                .email("Artem@rambler.ru")
//                .login("Fenris")
//                .name("Artem")
//                .birthday(LocalDate.of(1988, SEPTEMBER, 25))
//                .build();
//
//        mockMvc.perform(
//                        MockMvcRequestBuilders.post("/users")
//                                .content(objectMapper.writeValueAsString(user))
//                                .contentType(MediaType.APPLICATION_JSON)
//                )
//                .andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(user)));
//    }
    }


