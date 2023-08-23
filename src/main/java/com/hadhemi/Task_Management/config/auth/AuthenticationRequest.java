package com.hadhemi.Task_Management.config.auth;


import com.hadhemi.Task_Management.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {

    private String email;
    String password;

    //Role role;
}
