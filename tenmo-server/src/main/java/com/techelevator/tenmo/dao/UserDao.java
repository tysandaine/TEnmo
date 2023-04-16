package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.User;

import java.util.List;

public interface UserDao {

    List<User> findAll();

    List<User> findAllPrivate();

    User findByUsername(String username);

    int findIdByUsername(String username);

    boolean create(String username, String password);

    String getUsernameByAccountId(long accountId);
}
