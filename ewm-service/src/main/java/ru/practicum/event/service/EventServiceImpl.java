package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidateException;
import ru.practicum.request.repository.ParticipationRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.utils.PageConfig;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository requestRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    @Override
    public EventFullDto createEvent(Long userId, NewEventDto eventRequestDto) {
        log.debug("/create event");
        User existedUser = getExistedUser(userId);
        Category existedCategory = getExistedCategory(eventRequestDto.getCategory());
        checkEventDateConstraint(eventRequestDto);
        Event eventToSave = EventMapper.toModel(eventRequestDto);
        eventToSave.setInitiator(existedUser);
        eventToSave.setCategory(existedCategory);
        Event savedEvent = eventRepository.save(eventToSave);
        log.debug("Saved Event id: {}", savedEvent.getId());
        return EventMapper.toDto(savedEvent,0, 0);
    }

    @Transactional
    @Override // TODO !!!!!!!!!!
    public EventFullDto updateEventUser(Long userId, Long eventId, UpdateEventUserRequest eventDto) {
        return null;
    }

    @Transactional
    @Override
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventUserRequest updateEventDto) {
        log.debug("/update event admin");
        Event existedEvent = getExistedEvent(eventId);
        checkPublishedOnConstraint(updateEventDto, existedEvent);
        checkStateActionConstraint(updateEventDto, existedEvent);
        Long newCategoryId = updateEventDto.getCategory();
        Event updatedEvent = EventMapper.patchMappingToModel(
                updateEventDto,
                newCategoryId == null ? Optional.empty() : categoryRepository.findById(newCategoryId),
                existedEvent);
        Event savedEvent = eventRepository.save(updatedEvent);//TODO удалить
        Event savedEvent2 = eventRepository.findById(savedEvent.getId()).get();//TODO удалить
        return EventMapper.toDto(updatedEvent, 0, 0); // TODO запросы / статистика
    }

    @Override
    public List<EventFullDto> getEvents(List<Long> users,
                                        List<EventState> states,
                                        List<Long> categories,
                                        LocalDateTime rangeStart,
                                        LocalDateTime rangeEnd,
                                        Integer from,
                                        Integer size) {
        log.debug("/get events");
        PageRequest pageRequest = new PageConfig(from, size, Sort.unsorted());
        return eventRepository.getEvents(users, states, categories, rangeStart, rangeEnd, pageRequest).stream()
                .map(event -> EventMapper.toDto(event, 0, 0)) // TODO запросы / статистика
                .collect(Collectors.toList());
    }

    private void checkPublishedOnConstraint(UpdateEventUserRequest updateEventDto, Event existedEvent) {
        LocalDateTime actualPublishedOn = existedEvent.getPublishedOn();
        if (actualPublishedOn != null && !updateEventDto.getEventDate().isAfter(actualPublishedOn.plusHours(1)))
            throw new ValidateException("Event date must be later then publication date on 1 hour");
    }

    private void checkStateActionConstraint(UpdateEventUserRequest updateEventDto, Event existedEvent) {
        StateAction newStateAction = updateEventDto.getStateAction();
        EventState actualState = existedEvent.getState();
        if (newStateAction.equals(StateAction.PUBLISH_EVENT) && !actualState.equals(EventState.PENDING))
            throw new ValidateException("Cannot publish event because it's not in the right state. Need: PENDING");
        if (newStateAction.equals(StateAction.REJECT_EVENT) && !actualState.equals(EventState.PUBLISHED))
            throw new ValidateException("Cannot rejected event because it's not in the right state. Need: PUBLISHED");
    }

    private void checkEventDateConstraint(NewEventDto eventRequestDto) {
        final Instant now = Instant.now();
        final Instant eventDateTime = eventRequestDto.getEventDate().toInstant(ZoneOffset.UTC);
        long hoursDifference = ChronoUnit.HOURS.between(now, eventDateTime);
        if (hoursDifference < 2L) throw new ValidateException("Event date-time can't be early then 2 hours at now");
    }

    private Category getExistedCategory(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category id=" + catId + " not found"));
    }

    private User getExistedUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " not found"));
    }

    private Event getExistedEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found"));
    }
}