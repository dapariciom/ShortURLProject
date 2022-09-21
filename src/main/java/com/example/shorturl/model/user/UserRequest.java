package com.example.shorturl.model.user;

import com.example.shorturl.model.roles.RoleEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

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

    private List<RoleEntity> roles = new ArrayList<>();

    @NotNull
    private String password;

}
