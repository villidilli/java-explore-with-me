package ru.practicum.adminAPI.service;

import ru.practicum.adminAPI.dto.category.CategoryRequestDto;
import ru.practicum.adminAPI.dto.category.CategoryResponseDto;
import ru.practicum.adminAPI.dto.user.UserRequestDto;
import ru.practicum.adminAPI.dto.user.UserResponseDto;

import java.util.List;

public interface AdminService {
    UserResponseDto createUser(UserRequestDto userRequestDto);

    List<UserResponseDto> getAllUsers(Integer[] ids, Integer from, Integer size);

    void deleteUser(Long userId);

    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto updateCategory(Long catId, CategoryRequestDto categoryRequestDto);

    void deleteCategory(Long catId);
}