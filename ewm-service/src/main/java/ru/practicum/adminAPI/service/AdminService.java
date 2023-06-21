package ru.practicum.adminAPI.service;

import ru.practicum.adminAPI.dto.user.UserRequestDto;
import ru.practicum.adminAPI.dto.user.UserResponseDto;

import java.util.List;

public interface AdminService {
    UserResponseDto createUser(UserRequestDto userRequestDto);

    List<UserResponseDto> getAllUsers(Integer[] ids, Integer from, Integer size);

    void deleteUser(Long userId);
}
