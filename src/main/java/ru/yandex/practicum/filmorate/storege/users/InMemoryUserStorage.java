package ru.yandex.practicum.filmorate.storege.users;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ru.yandex.practicum.filmorate.validation.Validation.setUserLoginValidation;

@Component
public class InMemoryUserStorage implements UserStorage {
    private Integer generationId = 1;
    private final Map<Integer, User> users = new HashMap<>();
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserStorage.class);

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        setUserLoginValidation(user);
        user.setId(generationId++);
        users.put(user.getId(), user);
        log.info("User create: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            setUserLoginValidation(user);
            users.replace(user.getId(), user);
            log.info("User update: {}", user);
            return user;
        }
        log.warn("User not update: {}", user);
        throw new NotFoundException("User not found " + user);
    }

    @Override
    public void deleteUsers() {
        users.clear();
    }

    @Override
    public User getUser(Integer id) {
        if (id == null) {
            log.warn("Film id not integer: {}", id);
            throw new NullPointerException("Film id not integer");
        }
        if (users.containsKey(id)) {
            return users.get(id);
        } else {
            log.warn("User by id not found: {}", id);
            throw new NotFoundException(String.format("User by id %d not found", id));
        }
    }

    @Override
    public void setId(Integer id) {
        this.generationId = id;
    }
}