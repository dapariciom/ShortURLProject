package com.example.shorturl.model.payload.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Data
public class UserRequest {

    @NotNull(message = "username shouldn't be null")
    private String userName;

    @Email(message = "invalid email address")
    private String email;

    private String firstName;

    private String lastName;

    @NotNull
    private String password;

}
