package ru.practicum.event.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.EventRequestDto;
import ru.practicum.event.dto.EventResponseDto;

@Service
@Slf4j
@Transactional(readOnly = true)
public class EventServiceImpl implements EventService {
    @Override
    public EventResponseDto createEvent(Long userId, EventRequestDto eventRequestDto) {

    }
}