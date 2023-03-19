package ru.yandex.practicum.filmorate.storege.users;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class InMemoryUserStorage implements UserStorage {
    private Integer generationId = 1;
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public List<User> getUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User addUser(User user) {
        user.setId(generationId++);
        users.put(user.getId(), user);
        log.info("User create: {}", user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        if (users.containsKey(user.getId())) {
            users.replace(user.getId(), user);
            log.info("User update: {}", user);
            return user;
        }
        log.warn("User not update: {}", user);
        throw new NotFoundException("User not found " + user);
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
    public void addUserFriends(Integer id, Integer friendId) {

    }

    @Override
    public void deleteUserFriends(Integer id, Integer friendId) {

    }

    @Override
    public List<User> getUserFriends(Integer id) {
        return null;
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        return null;
    }
}