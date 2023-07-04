package ru.practicum.user.service;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;

import java.util.List;

public interface UserService {

    UserDto createUser(NewUserRequest userRequestDto);

    List<UserDto> getAllUsers(Long[] ids, Integer from, Integer size);

    void deleteUser(java.lang.Long userId);
}