package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.service.ParticipationRequestService;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateController {
    private final UserService userService;
    private final EventService eventService;
    private final ParticipationRequestService requestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@RequestParam Long userId,
                                                 @RequestParam Long eventId) {
        log.debug("/create participation request");
        log.debug("Income parameters: userId: {}, eventId: {}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @Valid @RequestBody NewEventDto eventRequestDto) {
        log.debug("/create event");
        log.debug("Income parameters: user id: {}, event dto: {}", userId, eventRequestDto.toString());
        return eventService.createEvent(userId, eventRequestDto);
    }
}