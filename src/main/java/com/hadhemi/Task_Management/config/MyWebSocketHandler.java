package com.hadhemi.Task_Management.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hadhemi.Task_Management.config.jwt.JwtService;
import com.hadhemi.Task_Management.exception.UserNotFoundException;
import com.hadhemi.Task_Management.models.Message;
import com.hadhemi.Task_Management.models.User;
import com.hadhemi.Task_Management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
@Component
public  class MyWebSocketHandler implements WebSocketHandler {

    private Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private  JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    public WebSocketSession getSessionByUserId(String userId) {
        WebSocketSession session = sessions.get(userId);
        if (session != null && session.isOpen()) {
            System.out.println("Session for user " + userId + " found and open.");
        } else {
            System.out.println("Session for user " + userId + " not found or not open.");
        }
        return session;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("WebSocket connection established: " + session.getId());
        Map<String, Object> attributes = session.getAttributes();
        String jwtToken = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJoYWRoZW15ZGhAZ21haWwuY29tIiwiaWF0IjoxNjkyNzgwMzY5LCJleHAiOjE2OTI4NjY3Njl9.WpHN6QzEKxaLMn62IYQNaXvxuUkToYx85YxoICe18_4";
        System.out.println(jwtToken);
        if (jwtToken != null) {
            String username = jwtService.extractUsername(jwtToken);
            System.out.println("Extracted username from JWT: " + username);
            String recipientEmail = "hanen@gmail.com";
            Optional<User> recipientUserOptional = userRepository.findByEmail(recipientEmail);
            if (recipientUserOptional.isPresent()) {
                User recipientUser = recipientUserOptional.get();
                String recipientId = recipientUser.getId();
                sessions.put(recipientId, session);
                System.out.println("Session for user " + recipientId + " stored.");
            }
        }
    }


//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        System.out.println("WebSocket connection established: " + session.getId());
//
//        // Define static sender and recipient IDs
//        String senderId = "64e12162c57d486d7bc86468"; // Example sender ID
//        String recipientId = "64e12162c57d486d7bc86468"; // Example recipient ID
//
//        // Store the session using the recipient ID
//        sessions.put(recipientId, session);
//
//        System.out.println("Session for user " + recipientId + " stored.");
//    }


    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        if (message instanceof TextMessage) {

            System.out.println("Received a text message: " + ((TextMessage) message).getPayload());
            handleTextMessage(session, (TextMessage) message);
            System.out.println("Received WebSocket message from session " + session.getId() + ": " + message.getPayload());
        } else {
            System.out.println("Received a non-text message: " + message.getPayload());
        }
    }


    private void handleTextMessage(WebSocketSession session, TextMessage message) throws IOException {
        String payload = message.getPayload();
        System.out.println("Received text message payload: " + payload);
        ObjectMapper objectMapper = new ObjectMapper();
        Message receivedMessage = objectMapper.readValue(message.getPayload(), Message.class);
        User sender = userRepository.findById(receivedMessage.getSenderId())
                .orElseThrow(() -> new UserNotFoundException("Sender not found"));
        receivedMessage.setSenderPhoto(sender.getProfileImage());
        receivedMessage.setSentAt(new Date());
        String updatedMessageJson = objectMapper.writeValueAsString(receivedMessage);
        String recipientId = receivedMessage.getRecipientId();
        WebSocketSession recipientSession = sessions.get(recipientId);
        if (recipientSession != null && recipientSession.isOpen()) {
            System.out.println("Sending updated message to recipient: " + updatedMessageJson);
            TextMessage responseMessage = new TextMessage(updatedMessageJson);
            recipientSession.sendMessage(responseMessage);

        }
    }
    private String extractUserIdFromSession(WebSocketSession session) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String userId = userDetails.getUsername(); // Assurez-vous que le nom d'utilisateur est l'ID utilisateur
            System.out.println("Extracted user ID from session: " + userId);
            return userId;
        } else {
            System.out.println("No authentication principal found in session");
            return null;
        }
    }
    @Override
    public void handleTransportError(WebSocketSession session, Throwable throwable) throws Exception {
        System.err.println("WebSocket transport error in session: " + session.getId());

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus closeStatus) throws Exception {
        sessions.remove(session.getId());
        System.out.println("WebSocket connection closed: " + session.getId() + " - Status: " + closeStatus);
        if (closeStatus == CloseStatus.NORMAL) {
            System.out.println("Connection closed normally.");
        } else {
            System.out.println("Connection closed with status: " + closeStatus);
        }

    }


    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
