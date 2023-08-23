package com.hadhemi.Task_Management.dto;

import com.hadhemi.Task_Management.models.User;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrganizationDTO {
    private String name;
    private String description;
    private User admin;
    private String adminUserId;
    private LocalDateTime creationDateTime;
    private List<UserDTO> members;
    private List<TaskDTO> tasks;


}

