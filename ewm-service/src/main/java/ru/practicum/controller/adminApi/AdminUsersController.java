package ru.practicum.controller.adminApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin")
public class AdminUsersController {
    private final UserService adminService;

    @GetMapping("/users")
    public List<UserDto> getAllUsers(@RequestParam(required = false) Long[] ids,
                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                     @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug("/Income parameters: userIds = {}, from = {}, size = {}", ids, from, size);
        return adminService.getAllUsers(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest userRequestDto) {
        log.debug("UserDto to create: {}", userRequestDto.toString());
        return adminService.createUser(userRequestDto);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.debug("/delete user");
        log.debug("Запрошено удаление user с id: {}", userId);
        adminService.deleteUser(userId);
    }
}