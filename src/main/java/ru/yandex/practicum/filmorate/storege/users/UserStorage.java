package ru.yandex.practicum.filmorate.storege.users;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUsers();

    User addUser(User user);

    User updateUser(User user);

    void deleteUsers();

    User getUser(Integer id);

    void setId(Integer id);
}
