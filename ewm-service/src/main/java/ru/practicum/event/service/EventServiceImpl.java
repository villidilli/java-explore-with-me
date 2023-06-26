package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.NewEventDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidateException;
import ru.practicum.request.repository.ParticipationRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository requestRepository;

    @Transactional
    @Override
    public EventFullDto createEvent(Long userId, NewEventDto eventRequestDto) {
        log.debug("/create event");
        Event savedEvent = eventRepository.save(eventValidate(userId, eventRequestDto));
        log.debug("Saved Event id: {}", savedEvent.getId());
        return EventMapper.toDto(savedEvent,0, 0);
    }

    private Event eventValidate(Long userId, NewEventDto eventDto) {
        User existedUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User id: " + userId + " not found"));
        final Instant now = Instant.now();
        final Instant eventDateTime = eventDto.getEventDate().toInstant(ZoneOffset.UTC);
        long hoursDifference = ChronoUnit.HOURS.between(now, eventDateTime);
        if (hoursDifference < 2L) throw new ValidateException("Event date-time can't be early then 2 hours at now");
        Event eventToSave = EventMapper.toModel(eventDto);
        eventToSave.setInitiator(existedUser);
        return eventToSave;
    }
}