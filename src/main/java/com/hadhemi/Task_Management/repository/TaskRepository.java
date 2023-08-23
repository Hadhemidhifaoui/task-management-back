package com.hadhemi.Task_Management.repository;

import com.hadhemi.Task_Management.models.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task, String> {
    List<Task> findByAssigneesContains(String userId);
}
