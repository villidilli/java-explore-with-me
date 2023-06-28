package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidateException;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.ParticipationRequestMapper;
import ru.practicum.request.model.PapticipationRequestState;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.repository.ParticipationRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.Objects;


@Service
@Slf4j
@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {
    private final ParticipationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    @Override
    public ParticipationRequestDto createRequest(Long userId, Long eventId) {
        log.debug("/create request");
        ParticipationRequest validatedRequestTosave = requestValidate(userId, eventId);
        ParticipationRequest savedRequest = requestRepository.save(validatedRequestTosave);
        log.debug("Saved Request id: {}", savedRequest.getId());
        return ParticipationRequestMapper.toDto(savedRequest);
    }

    private ParticipationRequest requestValidate(Long userId, Long eventId) {
        User existedUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));
        Event existedEvent = eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id: " + eventId + " not found"));

        ParticipationRequest existedRequest = requestRepository.findByRequester_IdIsAndEvent_IdIs(userId, eventId);
        if (Objects.equals(existedEvent.getInitiator().getId(), userId))
                                    throw new ValidateException("Initiator can't create request for his event");

        if (existedRequest != null) throw new ValidateException("Request already exist. Requester Id: " + userId +
                                                                " Event id: " + eventId +
                                                                 "Request id: " + existedRequest.getId());
        if (!existedEvent.getState().equals(EventState.PUBLISHED))
                                    throw new ValidateException("Event id: " + eventId + " not published");
        if (Objects.equals(existedEvent.getParticipantLimit(), requestRepository.countAllByEvent_IdIs(eventId)))
            throw new ValidateException("Reached the limit request . Limit: " + existedEvent.getParticipantLimit());

        ParticipationRequest requestToSave = ParticipationRequestMapper.toModel(existedUser, existedEvent);

        if (existedEvent.getRequestModeration() == Boolean.FALSE) {
            requestToSave.setStatus(PapticipationRequestState.APPROVED);
        }
        return requestToSave;
    }
}