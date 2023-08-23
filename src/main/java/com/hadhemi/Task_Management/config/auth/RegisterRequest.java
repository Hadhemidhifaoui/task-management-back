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
public class RegisterRequest {

    private String name;

    private String email;

    private String password;

    private  String position;

    private byte[] profileImage;


}
