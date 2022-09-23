package com.example.shorturl.service.user;

import com.example.shorturl.dao.RoleRepository;
import com.example.shorturl.dao.UserRepository;
import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.model.user.UserRequest;
import com.example.shorturl.utils.exceptions.UserException;
import com.google.common.collect.ImmutableSet;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements IUserService{

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserService(final UserRepository userRepository, final RoleRepository roleRepository){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public UserEntity signUp(UserRequest userRequest) throws UserException {

        UserEntity userEntity = UserEntity.builder()
                .userName(userRequest.getUserName())
                //TODO: Encode password
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .roles(ImmutableSet.of(roleRepository.findByName("ROLE_USER").get()))
                .build();

        return userRepository.save(userEntity);
    }

    public Optional<UserEntity> findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }

}
