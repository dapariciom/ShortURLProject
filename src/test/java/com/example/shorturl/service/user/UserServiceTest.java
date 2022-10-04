package com.example.shorturl.service.user;

import com.example.shorturl.dao.RoleRepository;
import com.example.shorturl.dao.UserRepository;
import com.example.shorturl.model.roles.ERole;
import com.example.shorturl.model.roles.RoleEntity;
import com.example.shorturl.model.user.UserRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;
    @Mock
    private RoleRepository roleRepository;


    @Test
    void testSignUpNewUser(){

        UserRequest userRequest = new UserRequest("dan", "dan@outlook.com", "dan", "dan", "dan");

        RoleEntity roleEntity = RoleEntity.builder()
                .id(2L)
                .name(ERole.ROLE_USER)
                .build();

        when(roleRepository.findByName(ERole.ROLE_USER)).thenReturn(Optional.of(roleEntity));

        userService.signUp(userRequest);

        verify(userRepository, times(1)).save(any());

    }

    @Test
    void testFindUserByName(){

        String findName = "admin";

        userService.findByUserName(findName);

        verify(userRepository, times(1)).findByUserName(any());

    }

}
