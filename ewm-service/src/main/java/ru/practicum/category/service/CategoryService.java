package ru.practicum.category.service;

import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.CategoryDto;

import java.util.List;

public interface CategoryService {

    CategoryDto createCategory(NewCategoryDto categoryRequestDto);

    CategoryDto updateCategory(Long catId, NewCategoryDto categoryRequestDto);

    void deleteCategory(Long catId);

    List<CategoryDto> getAllCategories(Integer from, Integer size);

    CategoryDto getCategoryById(Long catId);
}