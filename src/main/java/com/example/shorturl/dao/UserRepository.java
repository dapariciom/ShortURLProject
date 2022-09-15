package com.example.shorturl.dao;

import com.example.shorturl.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    /**
     * This method is used to look for a UserEntity with a specific
     * userName from the database
     *
     * @param userName This is the username to look for
     * @return Optional<UserEntity> This returns the found user entity
     */
    Optional<UserEntity> findByUserName(String userName);

    /**
     * This method is used to look for a UserEntity with a specific
     * email from the database
     *
     * @param email This is the email to look for
     * @return Optional<RoleEntity> This returns the found user entity
     */
    Optional<UserEntity> findByEmail(String email);

}
