package ru.practicum.event.service;

import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.EventState;
import ru.practicum.utils.EventViewSort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto createEvent(Long userId, NewEventDto eventRequestDto);

    List<EventFullDto> getEventsForAdmin(List<Long> users,
                                         List<EventState> states,
                                         List<Long> categories,
                                         LocalDateTime rangeStart,
                                         LocalDateTime rangeEnd,
                                         Integer from,
                                         Integer size);

    EventFullDto updateEventAdmin(Long eventId, UpdateEventUserRequest eventDto);

    List<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size);

    EventFullDto getEventById(Long eventId, HttpServletRequest request);

    List<EventShortDto> getEventsForPublic(String text, List<Long> categories, Boolean paid,
                                           LocalDateTime rangeStart, LocalDateTime rangeEnd, EventViewSort sort,
                                           Boolean onlyAvailable, Integer from, Integer size,
                                           HttpServletRequest request);
}