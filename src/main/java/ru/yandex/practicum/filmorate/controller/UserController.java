package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exeption.CustomValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

import ru.yandex.practicum.filmorate.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Min;


import static ru.yandex.practicum.filmorate.validation.Validation.isLoginUserValidation;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable("id") Integer id) {
        return userService.getUser(id);
    }

    @PostMapping
    public User createUser(@Valid @RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping
    public User uppDateUser(@Valid @RequestBody User user) throws CustomValidationException {

        return userService.updateUser(user);
    }

    @PutMapping("/{id}/friends/{friendId}")
    public Integer addFriend(@PathVariable("id") Integer id, @PathVariable("friendId") Integer friendId) {
        userService.addUserFriends(id, friendId);
        return friendId;
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public Integer deleteFriend(@PathVariable("id")  Integer id, @PathVariable("friendId")  Integer friendId) {
        userService.deleteUserFriends(id, friendId);
        return friendId;
    }

    @GetMapping("/{id}/friends")
    public List<User> getUserFriends(@PathVariable("id") Integer id) {
        return userService.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(@PathVariable("id") Integer id, @PathVariable("otherId") Integer otherId) {
        return userService.getCommonFriends(id, otherId);
    }
}
