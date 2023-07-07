package ru.practicum.controller.adminApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto categoryRequestDto) {
        log.debug("CategoryDto to create: {}", categoryRequestDto.toString());
        return categoryService.createCategory(categoryRequestDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.debug("/delete category");
        log.debug("Income parameters: catId: {}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @Valid @RequestBody NewCategoryDto categoryRequestDto) {
        log.debug("/update category");
        log.debug("Income parameters: catId: {}, body: {}", catId, categoryRequestDto.toString());
        return categoryService.updateCategory(catId, categoryRequestDto);
    }
}