package ru.practicum.adminAPI.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.adminAPI.dto.UserRequestDto;
import ru.practicum.adminAPI.dto.UserResponseDto;
import ru.practicum.adminAPI.service.AdminService;

import javax.validation.Valid;
import java.util.List;

import static ru.practicum.utils.UserMapper.toDto;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponseDto createUser(@Valid @RequestBody UserRequestDto userRequestDto) {
        log.debug("Входящий ДТО: {}", userRequestDto);
        return toDto(adminService.createUser(userRequestDto));
    }

    @GetMapping("/users")
    public List<UserResponseDto> getAllUsers()
}
