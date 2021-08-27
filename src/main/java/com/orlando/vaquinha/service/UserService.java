package com.orlando.vaquinha.service;

import java.util.Optional;

import com.orlando.vaquinha.model.User;

public interface UserService {
    void save(User User);

    Optional<User> findByUsername(String username);
}
