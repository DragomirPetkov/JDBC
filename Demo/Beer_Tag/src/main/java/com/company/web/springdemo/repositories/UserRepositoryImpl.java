package com.company.web.springdemo.repositories;

import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository{

    List<User> users;

    public UserRepositoryImpl(){
        this.users = new ArrayList<>();

        users.add(new User(1,"pesho","pass1",true));
        users.add(new User(2,"gosho","pass2",false));
        users.add(new User(3,"nadya","pass3",false));
    }
    @Override
    public List<User> getAll() {
        return new ArrayList<>(users);
    }

    @Override
    public User getById(int id) {
        return getAll().stream()
                .filter(user -> user.getId() == id)
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("User",id));
    }

    @Override
    public User getUsername(String username) {
        return getAll().stream()
                .filter(user -> user.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("User","username",username));
    }
}
