package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryRequestDto;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.dto.UserResponseDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService adminService;
    private final CategoryService categoryService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.debug("UserDto to create: {}", userRequestDto.toString());
        return adminService.createUser(userRequestDto);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        log.debug("CategoryDto to create: {}", categoryRequestDto.toString());
        return categoryService.createCategory(categoryRequestDto);
    }

    @GetMapping("/users")
    public List<UserResponseDto> getAllUsers(@RequestParam(required = false) Long[] ids,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        log.debug("/Income parameters: userIds = {}, from = {}, size = {}", ids, from, size);
        return adminService.getAllUsers(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.debug("/delete user");
        log.debug("Запрошено удаление user с id: {}", userId);
        adminService.deleteUser(userId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryResponseDto updateCategory(@PathVariable Long catId,
                                              @Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        log.debug("/update category");
        log.debug("Income parameters: catId: {}, body: {}", catId, categoryRequestDto.toString());
        return categoryService.updateCategory(catId, categoryRequestDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.debug("/delete category");
        log.debug("Income parameters: catId: {}", catId);
        categoryService.deleteCategory(catId);
    }
}