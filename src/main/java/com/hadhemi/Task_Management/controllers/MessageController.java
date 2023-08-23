package com.hadhemi.Task_Management.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hadhemi.Task_Management.config.MyWebSocketHandler;
import com.hadhemi.Task_Management.exception.UserNotFoundException;
import com.hadhemi.Task_Management.models.Message;
import com.hadhemi.Task_Management.models.User;
import com.hadhemi.Task_Management.repository.MessageRepository;
import com.hadhemi.Task_Management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MyWebSocketHandler myWebSocketHandler;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    @Autowired
    public MessageController(MyWebSocketHandler myWebSocketHandler, UserRepository userRepository, MessageRepository messageRepository) {
        this.myWebSocketHandler = myWebSocketHandler;
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
    }

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(@RequestBody Message request) {
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new UserNotFoundException("Sender not found"));
        User recipient = userRepository.findById(request.getRecipientId())
                .orElseThrow(() -> new UserNotFoundException("Recipient not found"));

        Message message = new Message(sender, recipient, request.getContent());
        message.setSenderPhoto(request.getSenderPhoto());
        message.setSentAt(new Date());
        messageRepository.save(message);
        WebSocketSession recipientSession = myWebSocketHandler.getSessionByUserId(request.getRecipientId());
        System.out.println("Message sent via WebSocket: " + message.getContent());
        if (recipientSession != null && recipientSession.isOpen()) {
            System.out.println("Recipient session for user " + request.getRecipientId() + " found and open.");
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                String messageJson = objectMapper.writeValueAsString(message);
                System.out.println("Message JSON: " + messageJson);
                TextMessage textMessage = new TextMessage(messageJson);
                recipientSession.sendMessage(textMessage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Recipient session for user " + request.getRecipientId() + " not found or not open.");
        }
        return ResponseEntity.ok("Message sent successfully");
    }



    @GetMapping("/history/{userId1}/{userId2}")
    public ResponseEntity<List<Message>> getChatHistory(
            @PathVariable String userId1,
            @PathVariable String userId2
    ) {
        List<Message> chatHistory1 = messageRepository.findBySenderAndRecipient(userId1, userId2);
        List<Message> chatHistory2 = messageRepository.findBySenderAndRecipient(userId2, userId1);

        List<Message> combinedChatHistory = new ArrayList<>();
        combinedChatHistory.addAll(chatHistory1);
        combinedChatHistory.addAll(chatHistory2);

         //Sort messages by sentAt timestamp
        //combinedChatHistory.sort(Comparator.comparing(Message::getSentAt));
        combinedChatHistory.sort(Comparator.comparing(Message::getSentAt, Comparator.nullsLast(Comparator.naturalOrder())));

        return ResponseEntity.ok(combinedChatHistory);
    }




}
