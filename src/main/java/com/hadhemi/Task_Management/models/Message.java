package com.hadhemi.Task_Management.models;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "chat_messages")
public class Message {

    @Id
    private String id;
    private String content;
    private String senderId;
    private String recipientId;

    @DBRef
    private User sender;

    public Message( User sender, User recipient, String content) {
        this.content = content;
        this.sender = sender;
        this.recipient = recipient;
    }

    @DBRef
    private User recipient;

    private byte[] senderPhoto;

    private Date sentAt;


}

