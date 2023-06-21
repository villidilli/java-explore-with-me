package ru.practicum.utils;

import lombok.experimental.UtilityClass;
import ru.practicum.adminAPI.dto.UserRequestDto;
import ru.practicum.adminAPI.dto.UserResponseDto;
import ru.practicum.adminAPI.model.User;

@UtilityClass
public class UserMapper {
    public User toModel(UserRequestDto dto) {
        User model = new User();
        model.setEmail(dto.getEmail());
        model.setName(dto.getName());
        return model;
    }

    public UserResponseDto toDto(User model) {
        UserResponseDto dto = new UserResponseDto();
        dto.setId(model.getId());
        dto.setEmail(model.getEmail());
        dto.setName(model.getName());
        return dto;
    }
}