package com.example.shorturl.controller.login;

import com.example.shorturl.model.roles.RoleEntity;
import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.model.user.UserRequest;
import com.example.shorturl.model.user.UserResponse;
import com.example.shorturl.security.JwtUtil;
import com.example.shorturl.security.MyUserDetailsService;
import com.example.shorturl.service.user.UserService;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @InjectMocks
    private AuthController authController;

    @Mock
    private UserService userService;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private MyUserDetailsService myUserDetailsService;
    @Mock
    private JwtUtil jwtUtil;

    @Test
    void testSignUpWithCorrectUser(){

        UserRequest userRequest = new UserRequest("dan", "dan@outlook.com", "dan", "dan", "dan");

        when(userService.findByUserName(any())).thenReturn(Optional.empty());

        RoleEntity roleEntity = RoleEntity.builder()
                .id(2L)
                .name("USER")
                .build();

        UserEntity userToReturn = UserEntity.builder()
                .id(1L)
                .userName(userRequest.getUserName())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .roles(ImmutableSet.of(roleEntity))
                .build();

        when(userService.signUp(any())).thenReturn(userToReturn);

        ResponseEntity<UserResponse> responseObtained = authController.signUp(userRequest);

        assertEquals(responseObtained.getBody().getUserName(), userRequest.getUserName());
        assertEquals(responseObtained.getStatusCode(), HttpStatus.CREATED);

    }

    @Test
    void testSignUpWithNullUser(){

        try{
            authController.signUp(null);
        } catch (ResponseStatusException e){
            assertEquals("User request is missing or empty", e.getReason());
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        }

    }

    @Test
    void testSignUpWithRepeatedUserName(){

        UserRequest userRequest = new UserRequest("dan", "dan@outlook.com", "dan", "dan", "dan");

        RoleEntity roleEntity = RoleEntity.builder()
                .id(2L)
                .name("USER")
                .build();

        UserEntity userToReturn = UserEntity.builder()
                .id(1L)
                .userName(userRequest.getUserName())
                .password(userRequest.getPassword())
                .email(userRequest.getEmail())
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .roles(ImmutableSet.of(roleEntity))
                .build();

        when(userService.findByUserName(userRequest.getUserName())).thenReturn(Optional.of(userToReturn));

        try{
            authController.signUp(userRequest);
        } catch (ResponseStatusException e){
            assertEquals("Username already exists", e.getReason());
            assertEquals(HttpStatus.BAD_REQUEST, e.getStatus());
        }

    }

}
