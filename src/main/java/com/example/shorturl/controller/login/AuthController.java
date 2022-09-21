package com.example.shorturl.controller.login;

import com.example.shorturl.model.auth.AuthenticationRequest;
import com.example.shorturl.model.auth.AuthenticationResponse;
import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.model.user.UserRequest;
import com.example.shorturl.model.user.UserResponse;
import com.example.shorturl.security.JwtUtil;
import com.example.shorturl.security.MyUserDetailsService;
import com.example.shorturl.service.user.UserService;
import com.example.shorturl.utils.exceptions.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
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
    private final JwtUtil jwtUtil;

    public AuthController(final UserService userService,
                          final AuthenticationManager authenticationManager,
                          final MyUserDetailsService myUserDetailsService,
                          final JwtUtil jwtUtil){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.myUserDetailsService = myUserDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/signup")
    public ResponseEntity<UserResponse> signUp(@Valid @RequestBody UserRequest userRequest) throws UserException {

        if(Objects.isNull(userRequest))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User request is missing or empty");

        Optional<UserEntity> userObtained = userService.findFirstUserByUserNameOrEmail(userRequest.getUserName(), userRequest.getEmail());

        if(userObtained.isPresent())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username or email already exists");

        UserEntity user = userService.signUp(userRequest);

        return new ResponseEntity<>(
                UserResponse.builder()
                        .userName(user.getUserName())
                        .build(), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception{
        try{
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(), authenticationRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect userName or password", e);
        }

        final UserDetails userDetails = myUserDetailsService
                .loadUserByUsername(authenticationRequest.getUserName());

        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse((jwt)));
    }

}
