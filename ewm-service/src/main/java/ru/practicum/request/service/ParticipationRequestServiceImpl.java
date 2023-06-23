package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.ParticipationRequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.repository.ParticipationRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;



@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        User existedUser = userRepository.findById(userId)
                        .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));
        Event existedEvent = eventRepository.findById(eventId)
                        .orElseThrow(() -> new NotFoundException("Event with id: " + eventId + " not found"));
        ParticipationRequest savedRequest =
                requestRepository.save(ParticipationRequestMapper.toModel(existedUser, existedEvent));
        log.debug("Saved Request id: {}", savedRequest.getId());
        return ParticipationRequestMapper.toDto(savedRequest);
    }
}