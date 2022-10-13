package com.example.shorturl.dao;

import com.example.shorturl.model.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
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
     * This method is used to look for the first UserEntity found with a specific
     * userName OR email from the database
     *
     * @param userName This is the username to look for
     * @param email This is the email to look for
     * @return Optional<UserEntity> This returns the found user entity
     */
    Optional<UserEntity> findFirstByUserNameOrEmail(String userName, String email);

}
