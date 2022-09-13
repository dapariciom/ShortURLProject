package com.example.shorturl.service.user;

import com.example.shorturl.dao.UserRepository;
import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.model.user.UserRequest;
import com.example.shorturl.utils.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;

    public UserService(final UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserEntity signUp(UserRequest userRequest) throws UserException {

        Optional<UserEntity> optionalUrl = findByUserName(userRequest.getUserName());

        if(optionalUrl.isPresent())
            throw new ResponseStatusException(HttpStatus.GONE, "user name already used");

        optionalUrl = findByEmail(userRequest.getEmail());

        if(optionalUrl.isPresent())
            throw new ResponseStatusException(HttpStatus.GONE, "email already used");

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

    private UserEntity save(UserEntity userEntity){
        return userRepository.save(userEntity);
    }

    private Optional<UserEntity> findByUserName(String userName){
        return userRepository.findByUserName(userName);
    };

    private Optional<UserEntity> findByEmail(String email){
        return userRepository.findByEmail(email);
    };

}
