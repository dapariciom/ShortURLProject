package com.example.shorturl.service.user;

import com.example.shorturl.dao.RoleRepository;
import com.example.shorturl.dao.UserRepository;
import com.example.shorturl.model.roles.RoleEntity;
import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.model.user.UserRequest;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class UserServiceTest {

    private UserService userService;

    private UserRepository userRepository;
    private RoleRepository roleRepository;

    @BeforeEach
    void setup(){

        this.userRepository = mock(UserRepository.class);
        this.roleRepository = mock(RoleRepository.class);

        this.userService = new UserService(userRepository, roleRepository);

    }

    @Test
    void test_signup_new_user(){

        UserRequest userRequest = new UserRequest("dan", "dan@outlook.com", "dan", "dan", "dan");

        RoleEntity roleUser = RoleEntity.builder()
                .id(2L)
                .name("ROLE_USER")
                .build();

        UserEntity userToSaved = UserEntity.builder()
                .userName(userRequest.getUserName())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .roles(ImmutableSet.of(roleUser))
                .build();

        UserEntity userToReturn = UserEntity.builder()
                .id(1L)
                .userName(userRequest.getUserName())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .roles(ImmutableSet.of(roleUser))
                .build();

        Mockito.when(this.roleRepository.findByName("ROLE_USER"))
                .thenReturn(Optional.of(roleUser));

        Mockito.when(this.userRepository.save(userToSaved)).thenReturn(userToReturn);

        UserEntity userReceived = userService.signUp(userRequest);

        UserEntity userExpected = UserEntity.builder()
                .id(1L)
                .userName(userRequest.getUserName())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .roles(ImmutableSet.of(roleUser))
                .build();

        assertEquals(userExpected, userReceived);

    }

}
