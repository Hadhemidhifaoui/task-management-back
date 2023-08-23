package com.hadhemi.Task_Management.models;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthMessage {
    private String type;
    private String token;

    // Getters and setters...
}

