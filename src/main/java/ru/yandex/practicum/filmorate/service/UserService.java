package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storege.users.UserStorage;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public List<User> getUsers() {
        return userStorage.getUsers();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUser(Integer id) {
        return userStorage.getUser(id);
    }

    public void addUserFriends(Integer id, Integer friendId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);
        user.getFriends().add(friendId);
        friend.getFriends().add(id);
    }

    public void deleteUserFriends(Integer id, Integer friendId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(friendId);
        if (user.getFriends().contains(friendId)) {
            user.getFriends().remove(friendId);
            friend.getFriends().remove(id);
        } else {
            throw new NotFoundException(String.format("This user id %d is not added as a friends", friendId));
        }
    }

    public List<User> getUserFriends(Integer id) {
        User user = userStorage.getUser(id);
        List <User> userFriends = new ArrayList<>();

        for (Integer userId : user.getFriends()) {
            if (userStorage.getUser(userId) != null) {
                userFriends.add(userStorage.getUser(userId));
            }
        }
        return userFriends;
    }

    public List<User> getCommonFriends(Integer id, Integer otherId) {
        User user = userStorage.getUser(id);
        User friend = userStorage.getUser(otherId);
        List<User> commonFriends = new ArrayList<>();

        for (Integer userId : user.getFriends()) {
            if (friend.getFriends().contains(userId)) {
                commonFriends.add(userStorage.getUser(userId));
            }
        }
        return commonFriends;
    }
}
