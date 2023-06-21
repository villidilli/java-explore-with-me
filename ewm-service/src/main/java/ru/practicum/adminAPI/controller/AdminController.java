package ru.practicum.adminAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.adminAPI.dto.category.CategoryRequestDto;
import ru.practicum.adminAPI.dto.user.UserRequestDto;
import ru.practicum.adminAPI.dto.user.UserResponseDto;
import ru.practicum.adminAPI.service.AdminService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.debug("UserDto to create: {}", userRequestDto);
        return adminService.createUser(userRequestDto);
    }

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponseDto createCategory(@Valid @RequestBody CategoryRequestDto categoryRequestDto) {
        log.debug("CategoryDto to create: {}", categoryRequestDto);
        return adminService.createCategory()
    }

    @GetMapping("/users")
    public List<UserResponseDto> getAllUsers(@RequestParam(required = false) Integer[] ids,
                                             @RequestParam(defaultValue = "0") Integer from,
                                             @RequestParam(defaultValue = "10") Integer size) {
        log.debug("/Входящие параметры: userIds = {}, from = {}, size = {}", ids, from, size);
        return adminService.getAllUsers(ids, from, size);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.debug("/delete user");
        log.debug("Запрошено удаление user с id: {}", userId);
        adminService.deleteUser(userId);
    }
}
