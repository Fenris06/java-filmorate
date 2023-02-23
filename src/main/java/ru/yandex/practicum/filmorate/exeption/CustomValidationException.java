package ru.yandex.practicum.filmorate.exeption;

public class CustomValidationException extends RuntimeException {
    public CustomValidationException(final String message) {
        super(message);
    }

}

