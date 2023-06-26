package ru.practicum.event.service;

import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventService {
    EventFullDto createEvent(Long userId, NewEventDto eventRequestDto);

    EventFullDto updateEvent(Long userId, Long eventId, UpdateEventUserRequest eventDto);

    List<EventFullDto> getEvents(Long[] users,
                                 EventState[] states,
                                 Long[] categories,
                                 LocalDateTime rangeStart,
                                 LocalDateTime rangeEnd,
                                 Integer from,
                                 Integer size);
}