package ru.yandex.practicum.filmorate.model;

import lombok.*;
import net.minidev.json.annotate.JsonIgnore;
;

import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class Film {
    private Integer id;
    @NotBlank(message = "Film name is blank!")
    private String name;
    @Size(max=200, message = "Film description is over 200 characters")
    private String description;
    @NotNull(message = "Film releaseDate is null")
    private LocalDate releaseDate;
    @PositiveOrZero
    private int duration;
    @JsonIgnore
    private final Set<Integer> likes = new TreeSet<>();


}

