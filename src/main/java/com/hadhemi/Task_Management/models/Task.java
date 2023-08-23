package com.hadhemi.Task_Management.models;

import com.hadhemi.Task_Management.enums.TaskStatus;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "tasks")
public class Task {
    @Id
    private String id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime creationDateTime;
    private LocalDateTime dueDateTime;
    private List<String> assignees;
    private String priority;



}
