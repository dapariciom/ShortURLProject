package com.example.shorturl.controller.login;

import com.example.shorturl.model.roles.RoleEntity;
import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.model.user.UserRequest;
import com.example.shorturl.model.user.UserResponse;
import com.example.shorturl.service.user.UserService;
import com.example.shorturl.utils.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/auth")
public class LogInController {

    private final UserService userService;

    public LogInController(final UserService userService){
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody UserRequest userRequest) throws UserException {

        if(Objects.isNull(userRequest))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User request is missing or empty");

        UserEntity user = userService.signUp(userRequest);

        return new ResponseEntity<>(
                UserResponse.builder()
                        .userName(user.getUserName())
                        .build(), HttpStatus.OK);
    }

    @PutMapping("/user/{userId}/role/{roleId}")
    public UserEntity addRoleToUser(@PathVariable Integer userId, @PathVariable Integer roleId){

        Optional<UserEntity> optionalUser = userService.findUserById(userId);
        UserEntity user = optionalUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        Optional<RoleEntity> optionalRole = userService.findRoleById(roleId);
        RoleEntity role = optionalRole.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Role not found"));
        user.addRole(role);

        return userService.saveUser(user);
    }

}
