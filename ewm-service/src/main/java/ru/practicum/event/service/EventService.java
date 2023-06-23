package ru.practicum.event.service;

import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.EventFullDto;

public interface EventService {
    EventFullDto createEvent(Long userId, NewEventDto eventRequestDto);
}