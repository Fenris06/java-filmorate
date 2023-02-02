package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.yandex.practicum.filmorate.validation.Validation;

import javax.validation.Valid;
import javax.validation.ValidationException;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/users")
public class UserController {
    private int generationId = 1;
    private final Map<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        if(user.getLogin().contains(" ")) {
            log.warn("Login cannot contain spaces : {}", user);
            throw new ValidationException("Login cannot contain spaces");
        }
        if (user.getName() == null) {
            user.setName(user.getLogin());
        }
        user.setId(generationId++);
        users.put(user.getId(), user);
        log.info("User create: {}", user);
        return user;
    }

    @PutMapping
    public User uppDateUser(@Valid @RequestBody User user) throws ValidationException {
        if(user.getLogin().contains(" ")) {
            log.warn("Login cannot contain spaces : {}", user);
            throw new ValidationException("Login cannot contain spaces");
        }
        if (users.containsKey(user.getId())) {
            if (user.getName().isBlank()) {
                user.setName(user.getEmail());
            }
            users.replace(user.getId(), user);
            log.info("User update: {}", user);
            return user;
        }
        log.warn("User not update: {}", user);
        throw new ValidationException("User are not update");
    }
}
