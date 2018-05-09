package com.taskmanager.api.dao;

import com.taskmanager.api.domains.User;

public interface UserDAO {

    User register(User user);

    User findOne(long id);

    User update(long id, User user);

    User findUserByUsername(String username);
}
