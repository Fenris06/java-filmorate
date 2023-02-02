package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;

@Data
@Builder
public class User {
    private int id;
    @NotBlank(message = "Email is blank!")
    @Email(message = "Email should be valid!")
    private String email;
    @NotBlank(message = "Login is blank!")
    private String login;
    private String name;
    @Past(message = "Birthday can't be in future!")
    private LocalDate birthday;
}
