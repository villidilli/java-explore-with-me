package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Override
    public EventFullDto createEvent(Long userId, NewEventDto eventRequestDto) {
        log.debug("/create event");
        User existedUser = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User id: " + userId + " not found"));
        Event newEvent = EventMapper.toModel(eventRequestDto);
        newEvent.setInitiator(existedUser);
        Event savedEvent = eventRepository.save(newEvent);
        log.debug("Saved Event id: {}", savedEvent.getId());
        return null;
//        return EventMapper.toDto(savedEvent, )
    }
}