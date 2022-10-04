package com.example.shorturl.controller.login;

import com.example.shorturl.model.auth.AuthenticationRequest;
import com.example.shorturl.model.auth.AuthenticationResponse;
import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.model.user.UserRequest;
import com.example.shorturl.model.user.UserResponse;
import com.example.shorturl.security.MyUserDetailsService;
import com.example.shorturl.security.jwt.JwtUtils;
import com.example.shorturl.service.user.UserService;
import com.example.shorturl.utils.exceptions.UserException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final MyUserDetailsService myUserDetailsService;
    private final JwtUtils jwtUtils;

    public AuthController(final UserService userService,
                          final AuthenticationManager authenticationManager,
                          final MyUserDetailsService myUserDetailsService,
                          final JwtUtils jwtUtils){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody UserRequest userRequest) throws UserException {

        if(Objects.isNull(userRequest))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User request is missing or empty");

        Optional<UserEntity> userObtained = userService.findByUserName(userRequest.getUserName());

        if(userObtained.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");

        UserEntity user = userService.signUp(userRequest);

        return new ResponseEntity<>(
                UserResponse.builder()
                        .userName(user.getUserName())
                        .build(), HttpStatus.CREATED);
    }

    @PostMapping("/user")
    public ResponseEntity<UserEntity> getUser(@Valid @RequestBody UserRequest userRequest) throws UserException {

        if(Objects.isNull(userRequest))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User request is missing or empty");

        Optional<UserEntity> optionalUser = userService.findByUserName(userRequest.getUserName());

        UserEntity user = optionalUser.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found"));

        HttpHeaders headers = new HttpHeaders();

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(user);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = myUserDetailsService
                .loadUserByUsername(authenticationRequest.getUserName());

        String jwt = jwtUtils.generateJwtToken(authentication);

        return new ResponseEntity<>(
                AuthenticationResponse.builder()
                        .jwt(jwt)
                        .build(), HttpStatus.OK);
    }

}
