package ru.practicum.category.model;

import lombok.experimental.UtilityClass;
import ru.practicum.category.dto.CategoryRequestDto;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.category.model.Category;

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