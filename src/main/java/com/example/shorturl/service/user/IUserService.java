package com.example.shorturl.service.user;

import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.model.user.UserRequest;

import java.util.Optional;

public interface IUserService {

    /**
     * This method is used to create a new user in the databases that will
     * be login with the future. This user is required for the authentication
     * and authorization of an account
     *
     * @param userRequest This request object contains the main information of the new user
     * @return Optional<UserEntity> This returns the entity created in the database
     */
    Optional<UserEntity> signUp(UserRequest userRequest);
}
