package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventResponseDto;
import ru.practicum.event.service.EventService;
import ru.practicum.user.service.UserService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateController {
    private final UserService userService;
    private final EventService eventService;

    @GetMapping("/{userId}/events")
    public List<EventResponseDto> getAllUserEvents(@PathVariable Long userId,
                                                   @RequestParam(defaultValue = "0") Integer from,
                                                   @RequestParam(defaultValue = "10") Integer size) {

    }
}
