package com.hadhemi.Task_Management.models;

import com.hadhemi.Task_Management.dto.UserDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "organizations")
public class Organization {
    @Id
    private String id;
    private String name;
    private String description;
    private User admin;
    private String adminUserId;
    private LocalDateTime creationDateTime;
    private List<User> members;
    private List<Task> tasks;


}

