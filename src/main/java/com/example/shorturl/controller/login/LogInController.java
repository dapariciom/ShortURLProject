package com.example.shorturl.controller.login;

import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.model.user.UserRequest;
import com.example.shorturl.model.user.UserResponse;
import com.example.shorturl.service.user.UserService;
import com.example.shorturl.utils.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/login")
public class LogInController {

    private final UserService userService;

    public LogInController(final UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signUp")
    public ResponseEntity<UserResponse> signUp(@RequestBody UserRequest userRequest) throws UserNotFoundException {

        if(Objects.isNull(userRequest))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User request is missing or empty");

        Optional<UserEntity> optionalUser = userService.signUp(userRequest);

        UserEntity user = optionalUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not created"));

        return new ResponseEntity<>(
                UserResponse.builder()
                        .userName(user.getUserName())
                        .build(), HttpStatus.OK);
    }

}
