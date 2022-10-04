package com.example.shorturl.security;

import com.example.shorturl.model.user.UserEntity;
import com.example.shorturl.service.user.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    final UserService userService;

    public MyUserDetailsService(final UserService userService){
        this.userService = userService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<UserEntity> optionalUser = userService.findByUserName(username);

        UserEntity user = optionalUser.orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        return new MyUserDetails(user);
    }



}
