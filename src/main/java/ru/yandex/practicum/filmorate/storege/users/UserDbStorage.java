package ru.yandex.practicum.filmorate.storege.users;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exeption.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;

import javax.swing.plaf.basic.BasicTreeUI;
import javax.xml.crypto.Data;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> getUsers() {
        String sqlQuery = "SELECT * FROM users";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser);
    }

    @Override
    public User addUser(User user) {
        String sqlQuery = "INSERT INTO users (email, login, name, birthday)" +
                "values(?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"user_id"});
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getLogin());
            stmt.setString(3, user.getName());
            stmt.setDate(4, Date.valueOf(user.getBirthday()));
            return stmt;
        }, keyHolder);
        if (keyHolder.getKey() != null) {
            user.setId((Integer) keyHolder.getKey());
        }
        return user;
    }

    @Override
    public User updateUser(User user) {
        String sqlQuery = "UPDATE users SET " +
                "email = ?, login = ?, name = ?, birthday = ? " +
                "WHERE user_id = ?";
        jdbcTemplate.update(sqlQuery,
                user.getEmail(),
                user.getLogin(),
                user.getName(),
                Date.valueOf(user.getBirthday()),
                user.getId());
        return user;
    }

    @Override
    public User getUser(Integer id) {
        String sqlQuery = "SELECT * FROM users WHERE user_id = ?";
        List<User> users = jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
        if(users.isEmpty()) {
            throw new NotFoundException("User not found");
        } else {
            return users.get(0);
        }
    }

    private User mapRowToUser(ResultSet rs, int rowNum) throws SQLException {
        return User.builder()
                .id(rs.getInt("user_id"))
                .email(rs.getString("email"))
                .login(rs.getString("login"))
                .name(rs.getString("name"))
                .birthday(rs.getDate("birthday").toLocalDate())
                        .build();
    }

    @Override
    public void addUserFriends(Integer id, Integer friendId) {
        String sqlQuery = "INSERT INTO friends (user_id, friend_id) " +
                "VALUES (?, ?)";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public void deleteUserFriends(Integer id, Integer friendId) {
        String sqlQuery = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?";
        jdbcTemplate.update(sqlQuery, id, friendId);
    }

    @Override
    public List<User> getUserFriends(Integer id) {
        String sqlQuery = "SELECT * FROM users AS us JOIN (SELECT friend_id" +
                " FROM friends WHERE user_id = ?) AS fr ON us.user_id = fr.friend_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id);
    }

    @Override
    public List<User> getCommonFriends(Integer id, Integer otherId) {
        String sqlQuery = "SELECT * FROM users AS us" +
                " JOIN (SELECT friend_id FROM friends WHERE user_id = ?) AS fr ON us.user_id = fr.friend_id" +
                " JOIN (SELECT friend_id FROM friends WHERE user_id = ?) AS nfr ON us.user_id = nfr.friend_id";
        return jdbcTemplate.query(sqlQuery, this::mapRowToUser, id, otherId);
    }

}
