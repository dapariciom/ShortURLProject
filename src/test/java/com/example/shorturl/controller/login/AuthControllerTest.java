package com.example.shorturl.controller.login;

import com.example.shorturl.model.auth.AuthenticationRequest;
import com.example.shorturl.model.auth.AuthenticationResponse;
import com.example.shorturl.model.roles.ERole;
import com.example.shorturl.model.roles.RoleEntity;
import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.model.user.UserRequest;
import com.example.shorturl.model.user.UserResponse;
import com.example.shorturl.security.MyUserDetails;
import com.example.shorturl.security.MyUserDetailsService;
import com.example.shorturl.security.jwt.JwtUtil;
import com.example.shorturl.service.user.UserService;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    @Test
    void testSignUpWithCorrectUser(){

        UserRequest userRequest = new UserRequest("dan", "dan@outlook.com", "dan", "dan", "dan");

        when(userService.findByUserName(any())).thenReturn(Optional.empty());

        RoleEntity roleEntity = RoleEntity.builder()
                .id(2L)
                .name(ERole.ROLE_USER)
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

        Executable executable = () -> authController.signUp(null);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, executable);

        assertEquals("User request is missing or empty", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

    }

    @Test
    void testSignUpWithRepeatedUserName(){

        UserRequest userRequest = new UserRequest("admin", "dan@outlook.com", "dan", "dan", "dan");

        RoleEntity roleEntity = RoleEntity.builder()
                .id(2L)
                .name(ERole.ROLE_USER)
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

        Executable executable = () -> authController.signUp(userRequest);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, executable);

        assertEquals("Username already exists", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());

    }

    @Test
    void testSignUpWithRepeatedEmail(){

        UserRequest userRequest = new UserRequest("dan", "admin@outlook.com", "dan", "dan", "dan");

        when(userService.findByUserName(any())).thenReturn(Optional.empty());

        String errorMessage = "Duplicate entry 'admin@outlook.com' for key 'users.email'";

        when(userService.signUp(any())).thenThrow(new DataIntegrityViolationException(errorMessage));

        Executable executable = () -> authController.signUp(userRequest);

        DataIntegrityViolationException exception = assertThrows(DataIntegrityViolationException.class, executable);

        assertEquals(errorMessage, exception.getMessage());

    }

    @Test
    void testLoginWithCorrectCredentials() {

        UserEntity user = new UserEntity(1L, "user", "user", "user@outlook.com", "user", "user", ImmutableSet.of(RoleEntity.builder()
                .id(2L)
                .name(ERole.ROLE_USER)
                .build()));

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(user.getUserName(), user.getPassword());

        MyUserDetails myUserDetails = new MyUserDetails(1L, user.getUserName(),user.getEmail(), user.getPassword(), user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList()));

        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ1c2VyIiwiZXhwIjoxNjY1MjAwMDQ0LCJpYXQiOjE2NjUxNjQwNDR9.V1XkSvuVZuxrJS5ZGAOdmPbC7YGhQlg2jhYQR6DwKM4";

        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        when(authentication.getPrincipal()).thenReturn(myUserDetails);

        when(jwtUtil.generateToken(any())).thenReturn(jwt);

        ResponseEntity<AuthenticationResponse> responseObtained = authController.createAuthenticationToken(authenticationRequest);

        assertEquals(responseObtained.getBody().getUserName(), user.getUserName());
        assertEquals(responseObtained.getBody().getEmail(), user.getEmail());
        assertEquals(responseObtained.getBody().getJwt(), jwt);
        assertEquals(responseObtained.getStatusCode(), HttpStatus.OK);
    }

    @Test
    void testLoginWithIncorrectCredentials() {

        UserEntity user = new UserEntity(1L, "user", "user", "user@outlook.com", "user", "user", ImmutableSet.of(RoleEntity.builder()
                .id(2L)
                .name(ERole.ROLE_USER)
                .build()));

        AuthenticationRequest authenticationRequest = new AuthenticationRequest(user.getUserName(), "password");

        String errorMessage = "Bad credentials";

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException(errorMessage));

        Executable executable = () -> authController.createAuthenticationToken(authenticationRequest);

        BadCredentialsException exception = assertThrows(BadCredentialsException.class, executable);

        assertEquals(errorMessage, exception.getMessage());
    }

}
