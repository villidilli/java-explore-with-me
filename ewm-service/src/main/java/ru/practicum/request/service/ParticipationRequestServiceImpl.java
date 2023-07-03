package ru.practicum.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;
import ru.practicum.exception.FieldConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidateException;
import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.dto.ParticipationRequestMapper;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.request.model.ParticipationRequestState;
import ru.practicum.request.repository.ParticipationRequestRepository;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.Collectors;


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
        User existedUser = getExistedUser(userId);
        Event existedEvent = getExistedEvent(eventId);
        ParticipationRequest validatedRequest = getValidatedRequest(existedUser, existedEvent);
        ParticipationRequest savedRequest = requestRepository.save(validatedRequest);
        log.debug("Saved Request id: {}", savedRequest.getId());
        return ParticipationRequestMapper.toDto(savedRequest);
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByUser(Long userId) {
        log.debug("/get requests by user");
        getExistedUser(userId);
        return requestRepository.findAllByRequester_Id(userId).stream()
                .map(ParticipationRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ParticipationRequestDto> getRequestsByUserAndEvent(Long userId, Long eventId) {
        log.debug("/get requests by event and user");
        getExistedUser(userId);
        getExistedEvent(eventId);
        return requestRepository.findAllByEvent_Id(eventId).stream()
                .map(ParticipationRequestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public ParticipationRequestDto cancelOwnRequest(Long userId, Long requestId) {
        log.debug("/cancel from own request");
        getExistedUser(userId);
        ParticipationRequest request = getExistedRequest(requestId);
        request.setStatus(ParticipationRequestState.CANCELED);
        requestRepository.save(request);
        return ParticipationRequestMapper.toDto(request);
    }

    //TODO WIP
    @Transactional
    @Override
    public EventRequestStatusUpdateResult changeRequestsStatus(Long userId, Long eventId,
                                                               EventRequestStatusUpdateRequest requestDto) {
        log.debug("/change event requests status");
        getExistedUser(userId);
        Event existedEvent = getExistedEvent(eventId);
        List<ParticipationRequest> existedRequests = requestRepository.findAllById(requestDto.getRequestIds());
        existedRequests.forEach(request -> checkConstraintParticipationLimit(existedEvent, request));
        existedRequests.forEach(request -> request.setStatus(requestDto.getStatus()));
        requestRepository.saveAll(existedRequests);
        EventRequestStatusUpdateResult result = new EventRequestStatusUpdateResult();
        switch (requestDto.getStatus()) {
            case CONFIRMED:
                result.getConfirmedRequests().addAll(existedRequests.stream()
                                                                    .map(ParticipationRequestMapper::toDto)
                                                                    .collect(Collectors.toList()));
                break;
            case REJECTED:
                result.getRejectedRequests().addAll(existedRequests.stream()
                                                                    .map(ParticipationRequestMapper::toDto)
                                                                    .collect(Collectors.toList()));
        }
        return result;
    }

    private ParticipationRequest getValidatedRequest(User existedUser, Event existedEvent) {
        ParticipationRequest existedRequest =
                requestRepository.findByRequester_IdIsAndEvent_IdIs(existedUser.getId(), existedEvent.getId());
        checkConstraintInitiatorRequesterEquals(existedUser, existedEvent);
        checkConstraintRequestExisted(existedRequest);
        checkConstraintPublished(existedEvent);
        ParticipationRequest mappedRequest = ParticipationRequestMapper.toModel(existedUser, existedEvent);
        checkConstraintParticipationLimit(existedEvent, mappedRequest);
        checkConstraintRequestModeration(existedEvent, mappedRequest);
        return mappedRequest;
    }

    private ParticipationRequest getExistedRequest(Long requestId) {
        return requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Request with id: " + requestId + " not found"));
    }

    private User getExistedUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));
    }

    private Event getExistedEvent(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id: " + eventId + " not found"));
    }

    private void checkConstraintInitiatorRequesterEquals(User existedUser, Event existedEvent) {
        if (Objects.equals(existedEvent.getInitiator().getId(), existedUser.getId()))
            throw new FieldConflictException("Initiator can't create request for his event");
    }

    private void checkConstraintRequestExisted(ParticipationRequest existedRequest) {
        if (existedRequest != null) throw new FieldConflictException("Request already exist");
    }

    private void checkConstraintPublished(Event existedEvent) {
        if (!existedEvent.getState().equals(EventState.PUBLISHED))
            throw new FieldConflictException("Event not published");
    }

    private void checkConstraintParticipationLimit(Event event, ParticipationRequest request) {
        if (event.getParticipantLimit() == 0) {//TODO было через ELSE
            request.setStatus(ParticipationRequestState.CONFIRMED);
            return;
        }
        Integer confirmedRequests =
                requestRepository.countAllByEvent_IdIsAndStatusIs(event.getId(), ParticipationRequestState.CONFIRMED);
        if (event.getParticipantLimit() == confirmedRequests) {
            throw new FieldConflictException("Reached the limit request . Limit: " + event.getParticipantLimit());
        }
//        if (event.getParticipantLimit() == requestRepository.countAllByEvent_IdIs(event.getId())) {
//            throw new FieldConflictException("Reached the limit request . Limit: " + event.getParticipantLimit());
//        }

    }

    private void checkConstraintRequestModeration(Event existedEvent, ParticipationRequest request) {
        if (existedEvent.getRequestModeration() == Boolean.FALSE) {
            request.setStatus(ParticipationRequestState.CONFIRMED);
        }
    }
}