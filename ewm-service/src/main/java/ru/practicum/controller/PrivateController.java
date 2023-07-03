package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.ParticipationRequestService;
import ru.practicum.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateController {
    private final UserService userService;
    private final EventService eventService;
    private final ParticipationRequestService requestService;

    // REQUESTS --->

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        log.debug("/create participation request");
        log.debug("Income parameters: userId: {}, eventId: {}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable Long userId) {
        log.debug("/get requests by user");
        return requestService.getRequestsByUser(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelOwnRequest(@PathVariable Long userId,
                                                    @PathVariable Long requestId) {
        log.debug("/cancel from own request");
        return requestService.cancelOwnRequest(userId, requestId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestsStatus(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @RequestBody
                                                               EventRequestStatusUpdateRequest requestDto) {
        log.debug("/change requests status");
        return requestService.changeRequestsStatus(userId, eventId, requestDto);
    }

    // EVENTS --->

    @PostMapping("/{userId}/events")
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto createEvent(@PathVariable Long userId,
                                    @Valid @RequestBody NewEventDto eventRequestDto) {
        log.debug("/create event");
        log.debug("Income parameters: user id: {}, event dto: {}", userId, eventRequestDto.toString());
        return eventService.createEvent(userId, eventRequestDto);
    }

    @GetMapping("/{userId}/events")
    public List<EventShortDto> getEventsByUser(@PathVariable Long userId,
                                               @RequestParam(defaultValue = "0") Integer from,
                                               @RequestParam(defaultValue = "10") Integer size) {
        return eventService.getEventsByUser(userId, from, size);
    }

    @PatchMapping("/{userId}/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long userId,
                                    @PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest eventDto) {
        log.debug("/update event");
        log.debug("Income userId: {}, eventId: {}, eventDto: {}", userId, eventId, eventDto.toString());
        return eventService.updateEventUser(userId, eventId, eventDto);
    }

    @GetMapping("/{userId}/events/{eventId}")
    public EventFullDto getEventByUser(@PathVariable Long userId,
                                       @PathVariable Long eventId) {
        log.debug("/get event by user");
        return eventService.getEventByUser(userId, eventId);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventUserRequests(@PathVariable Long userId,
                                                           @PathVariable Long eventId) {
        log.debug("/get requests by user and event");
        return requestService.getRequestsByUserAndEvent(userId, eventId);
    }
}