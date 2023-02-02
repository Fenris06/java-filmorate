package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Data
@Builder
public class Film {
    private int id;
    @NotBlank(message = "Film name is blank!")
    private String name;
    @Size(max=200, message = "Film description is over 200 characters")
    private String description;
    private LocalDate releaseDate;
    @PositiveOrZero
    private int duration;
}

