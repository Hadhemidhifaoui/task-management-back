package com.hadhemi.Task_Management.services;

import com.hadhemi.Task_Management.dto.UserDTO;
import com.hadhemi.Task_Management.models.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    UserDTO toDTO(User user);
    User toEntity(UserDTO userDTO);
    List<UserDTO> toDTOList(List<User> users);
}

