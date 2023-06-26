package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.service.CategoryService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PublicController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        log.debug("/get all categories");
        log.debug("Income parameters: from: {}, size: {}", from, size);
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.debug("/get category by id");
        log.debug("Income parameters: catId: {}", catId);
        return categoryService.getCategoryById(catId);
    }
}