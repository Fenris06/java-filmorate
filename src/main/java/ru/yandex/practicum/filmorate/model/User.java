package ru.yandex.practicum.filmorate.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class User {
    private Integer id;
    @NotBlank(message = "Email is blank!")
    @Email(message = "Email should be valid!")
    private String email;
    @NotEmpty
    private String login;
    private String name;
    @Past(message = "Birthday can't be in future!")
    private LocalDate birthday;
    @JsonIgnore
    private final Set<Integer> friends = new TreeSet<>();
}
