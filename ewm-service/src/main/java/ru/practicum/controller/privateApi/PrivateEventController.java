package ru.practicum.controller.privateApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class PrivateEventController {
    private final EventService eventService;

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
                                               @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                               @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug("/get events by user");
        log.debug("Income userId: {}", userId);
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
        log.debug("Income userId: {}, eventId: {}", userId, eventId);
        return eventService.getEventByUser(userId, eventId);
    }
}