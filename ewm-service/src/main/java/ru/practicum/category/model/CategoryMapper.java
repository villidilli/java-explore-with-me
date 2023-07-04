package ru.practicum.category.model;

import lombok.experimental.UtilityClass;

import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.CategoryDto;

@UtilityClass
public class CategoryMapper {

    public Category toModel(NewCategoryDto categoryRequestDto) {
        Category model = new Category();
        model.setName(categoryRequestDto.getName());
        return model;
    }

    public CategoryDto toDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}