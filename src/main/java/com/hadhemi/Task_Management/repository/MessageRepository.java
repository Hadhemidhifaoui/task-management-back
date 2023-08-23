package com.hadhemi.Task_Management.repository;

import com.hadhemi.Task_Management.models.Message;
import com.hadhemi.Task_Management.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findBySenderAndRecipient(String senderId, String recipientId);
}

