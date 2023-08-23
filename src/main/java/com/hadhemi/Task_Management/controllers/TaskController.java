package com.hadhemi.Task_Management.controllers;

import com.hadhemi.Task_Management.dto.TaskDTO;
import com.hadhemi.Task_Management.enums.TaskStatus;
import com.hadhemi.Task_Management.exception.UnauthorizedAccessException;
import com.hadhemi.Task_Management.models.Task;
import com.hadhemi.Task_Management.services.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@RestController
@Api("/task")
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;


    @GetMapping
    public List<Task> getAllTasks() {
        return taskService.getAllTasks();
    }




    @GetMapping("/assigned")
    public ResponseEntity<List<TaskDTO>> getTasksAssignedToUser(@RequestParam String userId) {
        List<TaskDTO> tasks = taskService.getTasksAssignedToUser(userId);
        return ResponseEntity.ok(tasks);
    }
    @PutMapping("/update-status")
    public ResponseEntity<?> updateTaskStatus(@RequestParam String taskId, @RequestParam TaskStatus newStatus) {
        try {
            taskService.updateTaskStatus(taskId, newStatus);
            return ResponseEntity.ok("{\"message\": \"Task status updated successfully\"}");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("{\"error\": \"Failed to update task status\"}");
        }
    }

    @PostMapping
    public ResponseEntity<TaskDTO> createTask(@RequestBody TaskDTO taskDTO) {
        TaskDTO createdTaskDTO = taskService.createTask(taskDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTaskDTO);
    }

    @GetMapping("/{taskId}")
    public ResponseEntity<TaskDTO> getTask(@PathVariable String taskId) {
        TaskDTO taskDTO = taskService.getTask(taskId);
        return ResponseEntity.ok(taskDTO);
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<TaskDTO> updateTask(@PathVariable String taskId, @RequestBody TaskDTO taskDTO) {
        TaskDTO updatedTaskDTO = taskService.updateTask(taskId, taskDTO);
        return ResponseEntity.ok(updatedTaskDTO);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable String taskId) {
        taskService.deleteTask(taskId);
        return ResponseEntity.noContent().build();
    }
}


