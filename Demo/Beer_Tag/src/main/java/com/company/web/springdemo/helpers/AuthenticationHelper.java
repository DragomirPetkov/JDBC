package com.company.web.springdemo.helpers;

import com.company.web.springdemo.exceptions.AuthorizationException;
import com.company.web.springdemo.exceptions.EntityNotFoundException;
import com.company.web.springdemo.models.User;
import com.company.web.springdemo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationHelper {

    private static final String AUTHORIZATION_HEADER_NAME = "Authorization";
    private static final String AUTHORIZATION_AUTHORIZATION_ERROR = "Invalid authentication.";

    private final UserService service;

    @Autowired
    public AuthenticationHelper(UserService service) {
        this.service = service;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey(AUTHORIZATION_HEADER_NAME)) {
            throw new AuthorizationException(AUTHORIZATION_AUTHORIZATION_ERROR);
        }
        try {

            String userInfo = headers.getFirst(AUTHORIZATION_HEADER_NAME);
            String username = getUsername(userInfo);
            String password = getPassword(userInfo);
            User user = service.getUsername(username);
            if (!user.getPassword().equals(password)){
                throw new AuthorizationException(AUTHORIZATION_AUTHORIZATION_ERROR);
            }
            return user;
        }catch (EntityNotFoundException e){
            throw new AuthorizationException(AUTHORIZATION_AUTHORIZATION_ERROR);
        }
    }

    private String getPassword(String userInfo) {
        int firstSpaceIndex = userInfo.indexOf(" ");
        if (firstSpaceIndex == -1){
            throw new AuthorizationException(AUTHORIZATION_AUTHORIZATION_ERROR);
        }
        return userInfo.substring(firstSpaceIndex + 1);
    }

    private String getUsername(String userInfo) {
        int firstSpaceIndex = userInfo.indexOf(" ");
        if (firstSpaceIndex == -1){
            throw new AuthorizationException(AUTHORIZATION_AUTHORIZATION_ERROR);
        }
        return userInfo.substring(0,firstSpaceIndex);
    }
}
