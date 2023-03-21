package ru.yandex.practicum.filmorate.service;

import ch.qos.logback.core.joran.conditional.IfAction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.CustomValidationException;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storege.users.UserStorage;

import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.validation.Validation.isLoginUserValidation;
import static ru.yandex.practicum.filmorate.validation.Validation.setUserLoginValidation;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserStorage userStorage;

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        if (isLoginUserValidation(user.getLogin())) {
            log.warn("Login cannot contain spaces : {}", user);
            throw new CustomValidationException("Login cannot contain spaces");
        }
        setUserLoginValidation(user);
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        if (isLoginUserValidation(user.getLogin())) {
            log.warn("Login cannot contain spaces : {}", user);
            throw new CustomValidationException("Login cannot contain spaces");
        }
        setUserLoginValidation(user);
        if (getUser(user.getId()) != null) {
            return userStorage.updateUser(user);
        } else {
            throw new NotFoundException("user not found");
        }
    }

    public User getUser(Integer id) {
        return userStorage.getUser(id);
    }

    public void addUserFriends(Integer id, Integer friendId) {
        if (getUser(id) != null && getUser(friendId) != null)
            userStorage.addUserFriends(id, friendId);
        else {
            throw new NotFoundException("user not found");
        }
    }

    public void deleteUserFriends(Integer id, Integer friendId) {
        if (getUser(id) != null && getUser(friendId) != null) {
            userStorage.deleteUserFriends(id, friendId);
        } else {
            throw new NotFoundException("user not found");
        }
    }

    public List<User> getUserFriends(Integer id) {
        if (getUser(id) != null) {
            return userStorage.getUserFriends(id);
        } else {
            throw new NotFoundException("user not found");
        }
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        if (getUser(id) != null && getUser(otherId) != null) {
            return userStorage.getCommonFriends(id, otherId);

        } else {
            throw new NotFoundException("user not found");
        }
    }
}

