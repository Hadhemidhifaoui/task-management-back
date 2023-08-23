package com.hadhemi.Task_Management.repository;

import com.hadhemi.Task_Management.enums.Role;
import com.hadhemi.Task_Management.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findByEmail(String email);
    User findByUsername(String username);
    List<User> findByRole(Role role);
    Optional<User> findById(String id);

}

