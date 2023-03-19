package ru.yandex.practicum.filmorate.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Builder
public class Genre {
    private Integer id;
    @NotBlank
    private String name;


}
