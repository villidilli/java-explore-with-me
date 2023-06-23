package ru.practicum.event.service;

import ru.practicum.event.dto.EventRequestDto;
import ru.practicum.event.dto.EventResponseDto;

public interface EventService {
    EventResponseDto createEvent(Long userId, EventRequestDto eventRequestDto);
}