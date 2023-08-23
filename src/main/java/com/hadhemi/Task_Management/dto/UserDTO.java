package com.hadhemi.Task_Management.dto;

import com.hadhemi.Task_Management.enums.Role;
import com.hadhemi.Task_Management.models.Organization;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Lob;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private  String id;
    private String name;
    private String email;
    private Role role;
    private Organization organization;
    private String organizationId;
    private Date creationDate;
    private String position;
    private String fullName;

    private String password;
    private boolean active;

    private boolean validated = true;

    private byte[] profileImage;
    private List<String> conversationIds;

    private List<String> permissions;


}

