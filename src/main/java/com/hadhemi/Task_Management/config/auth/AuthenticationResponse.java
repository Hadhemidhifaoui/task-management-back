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
public class AuthenticationResponse {

    private String token;

    private Role userRole;

    private String id ;
}
