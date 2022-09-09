package com.example.shorturl.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {

    private String userName;
    //TODO: Email validation
    private String email;
    private String firstName;
    private String lastName;
    private String roles;
    private String password;

}
