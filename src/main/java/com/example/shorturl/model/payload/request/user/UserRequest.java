package com.example.shorturl.model.payload.request.user;

import com.fasterxml.jackson.annotation.JsonProperty;
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

    @JsonProperty("user_name")
    @NotNull(message = "username shouldn't be null")
    private String userName;

    @Email(message = "invalid email address")
    private String email;

    @JsonProperty("first_name")
    private String firstName;

    @JsonProperty("last_name")
    private String lastName;

    @NotNull
    private String password;

}
