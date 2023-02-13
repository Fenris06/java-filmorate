package ru.yandex.practicum.filmorate.validation;



import lombok.Getter;


import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.time.Month.DECEMBER;


public class Validation {
    @Getter
    private static final LocalDate validateFilmDate = LocalDate.of(1895, DECEMBER, 28);
    private static final LocalDate validationBirthdayDate = LocalDate.now();



    public static boolean isFilmValidation(Film film) {

        if (film.getName().isBlank()) {
            return false;
        } else if (film.getDescription().length() > 200) {
            return false;
        } else if (film.getReleaseDate().isBefore(validateFilmDate)) {
            return false;
        } else if (film.getDuration() < 0) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isUserValidation(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            return false;
        } else if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            return false;
        } else if (user.getBirthday().isAfter(validationBirthdayDate)) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isFilmReleaseDateValidation(Film film) {
        if (film.getReleaseDate().isBefore(validateFilmDate)) {
            return false;
        }
        return true;
    }
    public static boolean isLoginUserValidation(String s) {
        Pattern p = Pattern.compile("\\s+");
        Matcher m = p.matcher(s);
        return m.find();
    }

    public static void setUserLoginValidation(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }


}
