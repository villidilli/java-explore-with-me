package ru.practicum.request.service;

import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

    ParticipationRequestDto createRequest(Long userId, Long eventId);

    List<ParticipationRequestDto> getRequestsByUser(Long userId);

    ParticipationRequestDto cancelOwnRequest(Long userId, Long requestId);

    EventRequestStatusUpdateResult changeRequestsStatus(Long userId, Long eventId,
                                                        EventRequestStatusUpdateRequest requestDto);

    List<ParticipationRequestDto> getRequestsByUserAndEvent(Long userId, Long eventId);
}