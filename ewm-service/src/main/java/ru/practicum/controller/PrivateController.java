package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventRequestDto;
import ru.practicum.event.dto.EventResponseDto;
import ru.practicum.event.service.EventService;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateController {
    private final UserService userService;
    private final EventService eventService;

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponseDto createEvent(@PathVariable Long userId,
                                        @Valid @RequestBody EventRequestDto eventRequestDto) {
        log.debug("/create event");
        log.debug("Income parameters: user id: {}, event dto: {}", userId, eventRequestDto.toString());
        return eventService.createEvent(userId, eventRequestDto);
    }
}