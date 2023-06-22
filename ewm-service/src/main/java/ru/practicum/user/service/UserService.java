package ru.practicum.user.service;

import ru.practicum.category.dto.CategoryRequestDto;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.dto.UserResponseDto;

import java.util.List;

public interface UserService {
    UserResponseDto createUser(UserRequestDto userRequestDto);

    List<UserResponseDto> getAllUsers(Long[] ids, Integer from, Integer size);

    void deleteUser(java.lang.Long userId);
}