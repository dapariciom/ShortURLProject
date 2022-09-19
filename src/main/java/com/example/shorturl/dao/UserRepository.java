package com.example.shorturl.dao;

import com.example.shorturl.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
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
     * This method is used to look for the UserEntities with a specific
     * userName or email from the database
     *
     * @param userName This is the userName to look for
     * @param email This is the email to look for
     * @return List<UserEntity> This returns the found user entities
     */
    List<UserEntity> findByUserNameOrEmail(String userName, String email);

}
