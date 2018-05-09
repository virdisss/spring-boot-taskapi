package com.taskmanager.api.services;

import com.taskmanager.api.dao.UserDAO;
import com.taskmanager.api.domains.User;
import com.taskmanager.api.repositories.UserRepo;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDAO {

    private UserRepo userRepo;

    public UserService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User register(User user) {
        return userRepo.save(user);
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepo.findByEmail(username);
    }

    @Override
    public User update(long id, User user) {

        User newUser = userRepo.getOne(id);
        newUser.setEmail(user.getEmail());
        newUser.setName(user.getName());
        newUser.setPassword(user.getPassword());
        return userRepo.save(newUser);
    }

    @Override
    public User findOne(long id) {
        return userRepo.findById(id).get();
    }
}
