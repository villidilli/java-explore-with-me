package ru.practicum.controller.adminApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;

import ru.practicum.utils.Constant;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/admin")
public class AdminEventController {
    private final EventService eventService;

    @GetMapping("/events")
    public List<EventFullDto> searchEvents(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = Constant.dateTimeFormat)
            LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = Constant.dateTimeFormat)
            LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug("/get events");
        log.debug("Income: users: {}, states: {}, categories: {}, rangeStart: {}, rangeEnd: {}, from: {}, size: {}",
                users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.searchEventsAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @Valid @RequestBody UpdateEventUserRequest eventDto) {
        log.debug("/updateEvent");
        log.debug("Income parameters: eventId: {}, eventDto: {}", eventId, eventDto.toString());
        return eventService.updateEventAdmin(eventId, eventDto);
    }
}