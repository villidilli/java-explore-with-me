package ru.practicum.user.model;

import lombok.experimental.UtilityClass;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

@UtilityClass
public class UserMapper {
    public User toModel(NewUserRequest dto) {
        User model = new User();
        model.setEmail(dto.getEmail());
        model.setName(dto.getName());
        return model;
    }

    public UserDto toDto(User model) {
        UserDto dto = new UserDto();
        dto.setId(model.getId());
        dto.setEmail(model.getEmail());
        dto.setName(model.getName());
        return dto;
    }
}