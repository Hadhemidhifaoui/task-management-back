package com.hadhemi.Task_Management.config.auth;



import com.hadhemi.Task_Management.config.jwt.JwtService;
import com.hadhemi.Task_Management.enums.Role;
import com.hadhemi.Task_Management.models.User;
import com.hadhemi.Task_Management.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    public AuthenticationResponse register(String name, String email, String password, String position , MultipartFile profileImage) {
        var user = User.builder()
                .name(name)
                .email(email)
                .position(position)
                .password(passwordEncoder.encode(password))
                .role(Role.USER)
                .creationDate(new Date())
                .validated(false)
                .build();


        repository.save(user);

        // If an image is provided, set the profile image
        if (profileImage != null) {
            try {
                user.setProfileImage(profileImage.getBytes());
            } catch (IOException e) {
                // Handle the exception
            }
        }

        // Generate JWT token and return the authentication response
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userRole(Role.USER)
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );
        var user = repository.findByEmail(request.getEmail())
                .orElseThrow();

        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .userRole(user.getRole())
                .id(user.getId())
                .build();


    }


}
