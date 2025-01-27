package org.example.service;

import org.example.model.User;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationHandler {
    private final Map<String, User> users;

    public AuthenticationHandler() {
        this.users = new HashMap<>();
    }

    public User register(String username, String password) throws IllegalArgumentException {
        if (users.containsKey(username)) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        User newUser = new User(username, password);
        users.put(username, newUser);
        return newUser;
    }

    public User login(String username, String password) throws IllegalArgumentException {
        User user = users.get(username);

        if (user == null) {
            throw new IllegalArgumentException("Пользователь с логином " + username + "не найден");
        }

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("Неверный пароль");
        }

        return user;
    }
}