package ru.yandex.practicum.filmorate.storege.users;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    List<User> getUsers();

    User addUser(User user);

    User updateUser(User user);

    User getUser(Integer id);

     void addUserFriends(Integer id, Integer friendId);

    void deleteUserFriends(Integer id, Integer friendId);

     List<User> getUserFriends(Integer id);

     List<User> getCommonFriends(Integer id, Integer otherId);
}
