package com.company.web.springdemo.services;

import com.company.web.springdemo.exceptions.AuthorizationException;
import com.company.web.springdemo.exceptions.EntityDuplicateException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.Beer;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.repositories.BeerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BeerServiceImpl implements BeerService {

    private static final String MODIFY_BEER_ERROR_MESSAGE = "Only admins and users that create beer can modify beer!";
    private final BeerRepository repository;

    @Autowired
    public BeerServiceImpl(BeerRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Beer> get(String name, Double minAbv, Double maxAbv, Integer styleId, String sortBy, String sortOrder) {
        return repository.getByName(name, minAbv, maxAbv, styleId, sortBy, sortOrder);
    }

    @Override
    public Beer get(int id) {
        return repository.getByName(id);
    }

    @Override
    public Beer getByName(String name) {
        return repository.getByName(name);
    }

    @Override
    public void create(Beer beer, User user) {
        boolean duplicateExists = true;
        try {
            repository.getByName(beer.getName());
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Beer", "name", beer.getName());
        }
        beer.setCreateBy(user);
        repository.create(beer);
    }

    @Override
    public void update(Beer beer, User user) {
        boolean duplicateExists = true;
        try {
            Beer existingBeer = repository.getByName(beer.getName());
            if (existingBeer.getId() == beer.getId()) {
                duplicateExists = false;
            }
        } catch (EntityNotFoundException e) {
            duplicateExists = false;
        }

        if (duplicateExists) {
            throw new EntityDuplicateException("Beer", "name", beer.getName());
        }

        checkModifyPermission(repository.getByName(beer.getId()), user);
        repository.update(beer);
    }

    @Override
    public void delete(int id, User user) {
        checkModifyPermission(repository.getByName(id), user);
        repository.delete(id);
    }

    private void checkModifyPermission(Beer beer, User user) {
        if (!user.isAdmin()) {
            if (!beer.getCreateBy().equals(user)) {
                throw new AuthorizationException(MODIFY_BEER_ERROR_MESSAGE);
            }
        }
    }
}




