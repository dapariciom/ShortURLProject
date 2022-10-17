package com.example.shorturl.model.payload.response.user;

import com.example.shorturl.model.roles.ERole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class AdminUserResponse {

    private String userName;

    private String password;

    private String email;

    private Set<ERole> roles;

}
