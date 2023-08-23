package com.hadhemi.Task_Management.services;

import com.hadhemi.Task_Management.dto.TaskDTO;
import com.hadhemi.Task_Management.enums.TaskStatus;
import com.hadhemi.Task_Management.exception.ResourceNotFoundException;
import com.hadhemi.Task_Management.exception.UnauthorizedAccessException;
import com.hadhemi.Task_Management.models.Task;
import com.hadhemi.Task_Management.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Autowired
    private TaskRepository taskRepository;

    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }


    public List<TaskDTO> getTasksAssignedToUser(String userId) {
        List<Task> tasks = taskRepository.findByAssigneesContains(userId);
        return convertToTaskDTOList(tasks);
    }
    @Transactional
    public void updateTaskStatus(String taskId, TaskStatus newStatus) {
        Task task = taskRepository.findById(taskId).orElse(null);
        if (task != null) {
            task.setStatus(newStatus);
            taskRepository.save(task);
        }
    }

    public TaskDTO createTask(TaskDTO taskDTO) {
        Task task = convertToTask(taskDTO);
        task.setStatus(TaskStatus.TO_DO); // Définir le statut initial
        Task createdTask = taskRepository.save(task);
        return convertToTaskDTO(createdTask);
    }

    public TaskDTO getTask(String taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));
        return convertToTaskDTO(task);
    }

    public TaskDTO updateTask(String taskId, TaskDTO updatedTaskDTO) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        updateTaskFromDTO(task, updatedTaskDTO);

        Task updatedTask = taskRepository.save(task);
        return convertToTaskDTO(updatedTask);
    }

    public void deleteTask(String taskId) {
        taskRepository.deleteById(taskId);
    }

    public static TaskDTO convertToTaskDTO(Task task) {
        TaskDTO taskDTO = new TaskDTO();
        taskDTO.setId(task.getId());
        taskDTO.setTitle(task.getTitle());
        taskDTO.setDescription(task.getDescription());
        taskDTO.setStatus(task.getStatus());
        taskDTO.setCreationDateTime(task.getCreationDateTime());
        taskDTO.setDueDateTime(task.getDueDateTime());
        taskDTO.setAssignees(task.getAssignees());
        taskDTO.setPriority(task.getPriority());

        return taskDTO;
    }

    public static Task convertToTask(TaskDTO taskDTO) {
        Task task = new Task();
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setCreationDateTime(taskDTO.getCreationDateTime());
        task.setDueDateTime(taskDTO.getDueDateTime());
        task.setAssignees(taskDTO.getAssignees());
        task.setPriority(taskDTO.getPriority());

        return task;
    }

    public static  List<TaskDTO> convertToTaskDTOList(List<Task> taskList) {
        return taskList.stream()
                .map(TaskService::convertToTaskDTO)
                .collect(Collectors.toList());
    }

    public static List<Task> convertToTaskList(List<TaskDTO> taskDTOList) {
        return taskDTOList.stream()
                .map(TaskService::convertToTask)
                .collect(Collectors.toList());
    }



    private void updateTaskFromDTO(Task task, TaskDTO taskDTO) {
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setStatus(taskDTO.getStatus());
        task.setCreationDateTime(taskDTO.getCreationDateTime());
        task.setDueDateTime(taskDTO.getDueDateTime());
        task.setAssignees(taskDTO.getAssignees());
        task.setPriority(taskDTO.getPriority());

    }
}

