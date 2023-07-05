package ru.practicum.controller.publicApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;

import ru.practicum.utils.Constant;
import ru.practicum.utils.EventViewSort;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
public class PublicEventController {
    private final EventService eventService;

    @GetMapping("/events")
    public List<EventShortDto> getAllEvents(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false)
            @DateTimeFormat(pattern = Constant.dateTimeFormat) LocalDateTime rangeStart,
            @RequestParam(defaultValue = Constant.unreachableEnd)
            @DateTimeFormat(pattern = Constant.dateTimeFormat) LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false) EventViewSort sort,
            @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(defaultValue = "10") @Positive Integer size,
            HttpServletRequest request) {
        log.debug("/get all events");
        return eventService.publicSearchEvents(text, categories, paid, rangeStart, rangeEnd,
                sort, onlyAvailable, from, size, request);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable("id") Long eventId,
                                     HttpServletRequest request) {
        log.debug("/get event by id");
        log.debug("Income eventId: {}", eventId);
        return eventService.getEventById(eventId, request);
    }
}