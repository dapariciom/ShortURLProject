package com.example.shorturl.service.user;

import com.example.shorturl.dao.UserRepository;
import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.model.user.UserRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public Optional<UserEntity> signUp(UserRequest userRequest){

        //TODO: Not duplicates of username and email
        UserEntity userEntity = UserEntity.builder()
                .userName(userRequest.getUserName())
                //TODO: Encode password
                .password(userRequest.getPassword())
                .roles(userRequest.getRoles())
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .build();

        return save(userEntity);
    }

    private Optional<UserEntity> save(UserEntity userEntity){
        return Optional.ofNullable(userRepository.save(userEntity));
    }

    public Optional<UserEntity> findByUserName(String userName){
        return userRepository.findByUserName(userName);
    };

    public Optional<UserEntity> findByEmail(String email){
        return userRepository.findByEmail(email);
    };

}
