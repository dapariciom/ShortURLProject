package com.example.shorturl.model.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserRequest {

    private String userName;
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private String roles;
    private String password;

}
