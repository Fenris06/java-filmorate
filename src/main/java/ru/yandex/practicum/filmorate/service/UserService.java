package ru.yandex.practicum.filmorate.service;

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
        if(getUser(user.getId()).getId() == user.getId()) {
            return userStorage.updateUser(user);
        } else {
            throw new NotFoundException("user id not found");
        }
    }

    public User getUser(Integer id) { // пытаюсь проверить юзера на null но вместо
                               // NotfoundExeption возвращается exception c 500 статусом при этом аналогичная провера классе
                              // filmServis проходит ок
        User user = userStorage.getUser(id);
       if(user != null) {
           return user;
       } else {
           throw new NotFoundException("user not found");
       }
    }

    public void addUserFriends(Integer id, Integer friendId) {
        try {
            userStorage.addUserFriends(id, friendId);
        } catch (Exception e) {
            throw new NotFoundException("user not found");
        }
    }

    public void deleteUserFriends(Integer id, Integer friendId) {
        try {
            userStorage.deleteUserFriends(id, friendId);
        } catch (Exception e) {
            throw new NotFoundException("user not found");
        }
    }

    public List<User> getUserFriends(Integer id) {
        return userStorage.getUserFriends(id);
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        return userStorage.getCommonFriends(id, otherId);
    }
}

