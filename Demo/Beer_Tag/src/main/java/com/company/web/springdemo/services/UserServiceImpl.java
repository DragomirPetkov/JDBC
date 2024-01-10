package com.company.web.springdemo.services;

import com.company.web.springdemo.models.User;
import com.company.web.springdemo.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService{

    private final UserRepository repository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository){
        this.repository = userRepository;
    }
    @Override
    public List<User> getAll() {
        return repository.getAll();
    }

    @Override
    public User getById(int id) {
        return repository.getById(id);
    }

    @Override
    public User getUsername(String username) {
        return repository.getUsername(username);
    }
}
