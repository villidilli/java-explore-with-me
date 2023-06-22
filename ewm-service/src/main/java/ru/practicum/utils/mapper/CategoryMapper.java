package ru.practicum.utils.mapper;

import lombok.experimental.UtilityClass;
import ru.practicum.adminAPI.dto.category.CategoryRequestDto;
import ru.practicum.adminAPI.dto.category.CategoryResponseDto;
import ru.practicum.adminAPI.model.Category;

@UtilityClass
public class CategoryMapper {

    public Category toModel(CategoryRequestDto categoryRequestDto) {
        Category model = new Category();
        model.setName(categoryRequestDto.getName());
        return model;
    }

    public CategoryResponseDto toDto(Category category) {
        CategoryResponseDto dto = new CategoryResponseDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}