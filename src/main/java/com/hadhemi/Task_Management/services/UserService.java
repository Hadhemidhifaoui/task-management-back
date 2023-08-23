package com.hadhemi.Task_Management.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hadhemi.Task_Management.dto.UserDTO;
import com.hadhemi.Task_Management.enums.Role;
import com.hadhemi.Task_Management.exception.ResourceNotFoundException;
import com.hadhemi.Task_Management.exception.UserNotFoundException;
import com.hadhemi.Task_Management.models.User;
import com.hadhemi.Task_Management.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<UserDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }




    private UserDTO convertToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .password(user.getPassword())
                .role(user.getRole())
                .fullName(user.getFullName())
                .active(user.isActive())
                .validated(user.isValidated())
                .profileImage(user.getProfileImage())
                .organization(user.getOrganization())
                .organizationId(user.getOrganizationId())
                .creationDate(user.getCreationDate())
                .position(user.getPosition())
                .permissions(user.getPermissions())
                .build();
    }

//    public UserDTO createUser(UserDTO userDTO) {
//        User user = convertToUser(userDTO);
//        user.setPassword(userDTO.getPassword(), passwordEncoder);
//        User createdUser = userRepository.save(user);
//        return convertToDTO(createdUser);
//    }



    public UserDTO createUserWithImageAndPermissions(String name, String email, String password, String position, MultipartFile profileImage, List<String> permissions) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password, passwordEncoder);
        user.setPosition(position);
        user.setCreationDate(new Date());
        user.setRole(Role.USER);

        if (permissions != null && !permissions.isEmpty()) {
            user.setPermissions(permissions);
        }

        try {
            user.setProfileImage(profileImage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }

        User createdUser = userRepository.save(user);
        return convertToDTO(createdUser);
    }

    public UserDTO createAdminWithImage(String name, String email, String password, String position, MultipartFile profileImage) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password, passwordEncoder); // Utiliser le mot de passe fourni
        user.setPosition(position);
        user.setCreationDate(new Date());
        user.setRole(Role.ORG_ADMIN);

        try {
            user.setProfileImage(profileImage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            // Gérer l'exception d'entrée/sortie si nécessaire
        }

        User createdUser = userRepository.save(user);
        return convertToDTO(createdUser);
    }
    public List<UserDTO> getUsersByRole(Role role) {
        List<User> users = userRepository.findByRole(role);
        return convertToUserDTOList(users);
    }

    public UserDTO getUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return convertToDTO(user);
    }

    public UserDTO updateUser(String userId, UserDTO updatedUserDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        updateUserFromDTO(user, updatedUserDTO);
        user.setPassword(updatedUserDTO.getPassword(), passwordEncoder); // Utilise la nouvelle méthode setPassword
        User updatedUser = userRepository.save(user);
        return convertToDTO(updatedUser);
    }
    public User updateUserStatus(String userId, boolean isValidated) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        user.setValidated(isValidated);
        return userRepository.save(user);
    }
    public void deleteUser(String userId) {
        userRepository.deleteById(userId);
    }

    private User convertToUser(UserDTO userDTO) {
        User user = new User();
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        user.setFullName(userDTO.getFullName());
        user.setActive(userDTO.isActive());
        user.setValidated(userDTO.isValidated());
        user.setProfileImage(userDTO.getProfileImage());
        user.setOrganization(userDTO.getOrganization());
        user.setOrganizationId(userDTO.getOrganizationId());
        user.setCreationDate(userDTO.getCreationDate());
        user.setPosition(userDTO.getPosition());
        user.setPermissions(userDTO.getPermissions());

        return user;
    }

    private void updateUserFromDTO(User user, UserDTO userDTO) {
        user.setName(userDTO.getName());
        user.setEmail(userDTO.getEmail());
        user.setRole(userDTO.getRole());
        user.setFullName(userDTO.getFullName());
        user.setActive(userDTO.isActive());
        user.setValidated(userDTO.isValidated());
        user.setProfileImage(userDTO.getProfileImage());
        user.setOrganization(userDTO.getOrganization());
        user.setOrganizationId(userDTO.getOrganizationId());
        user.setCreationDate(userDTO.getCreationDate());
        user.setPosition(userDTO.getPosition());
        user.setPermissions(userDTO.getPermissions());
    }

    private List<UserDTO> convertToUserDTOList(List<User> userList) {
        return userList.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private List<User> convertToUserList(List<UserDTO> userDTOList) {
        return userDTOList.stream()
                .map(this::convertToUser)
                .collect(Collectors.toList());
    }
}
