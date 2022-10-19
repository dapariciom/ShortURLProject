package com.example.shorturl.controller.admin;

import com.example.shorturl.model.payload.request.user.AdminUserRequest;
import com.example.shorturl.model.payload.response.user.AdminUserResponse;
import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    public AdminController(final UserService userService){
        this.userService = userService;
    }

    @PostMapping("/users")
    public ResponseEntity<AdminUserResponse> createUser(@Valid @RequestBody AdminUserRequest userRequest) {

        if(Objects.isNull(userRequest))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User request is missing or empty");

        UserEntity user = userService.adminCreateUser(userRequest);

        return new ResponseEntity<>(
                AdminUserResponse.builder()
                        .userName(user.getUserName())
                        .password(user.getPassword())
                        .email(user.getEmail())
                        .roles(user.getRoles().stream().map(role -> role.getName()).collect(Collectors.toSet()))
                        .build(), HttpStatus.CREATED);
    }

}