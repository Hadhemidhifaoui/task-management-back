package com.hadhemi.Task_Management.dto;

import com.hadhemi.Task_Management.enums.TaskStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskDTO {
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime creationDateTime;
    private LocalDateTime dueDateTime;
    private List<String> assignees;
    private String priority;

}

