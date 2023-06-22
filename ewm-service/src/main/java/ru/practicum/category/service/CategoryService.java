package ru.practicum.category.service;

import ru.practicum.category.dto.CategoryRequestDto;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.dto.UserResponseDto;

import java.util.List;

public interface CategoryService {

    CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto);

    CategoryResponseDto updateCategory(Long catId, CategoryRequestDto categoryRequestDto);

    void deleteCategory(Long catId);
}