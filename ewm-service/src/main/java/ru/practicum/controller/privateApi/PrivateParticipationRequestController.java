package ru.practicum.controller.privateApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.request.dto.EventRequestStatusUpdateRequest;
import ru.practicum.request.dto.EventRequestStatusUpdateResult;
import ru.practicum.request.dto.ParticipationRequestDto;
import ru.practicum.request.service.ParticipationRequestService;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@Validated
@RequestMapping("/users")
public class PrivateParticipationRequestController {
    private final ParticipationRequestService requestService;

    @PostMapping("/{userId}/requests")
    @ResponseStatus(HttpStatus.CREATED)
    public ParticipationRequestDto createRequest(@PathVariable Long userId,
                                                 @RequestParam Long eventId) {
        log.debug("/create participation request");
        log.debug("Income parameters: userId: {}, eventId: {}", userId, eventId);
        return requestService.createRequest(userId, eventId);
    }

    @GetMapping("/{userId}/requests")
    public List<ParticipationRequestDto> getRequestsByUser(@PathVariable Long userId) {
        log.debug("/get requests by user");
        log.debug("Income userId: {}", userId);
        return requestService.getRequestsByUser(userId);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    public ParticipationRequestDto cancelOwnRequest(@PathVariable Long userId,
                                                    @PathVariable Long requestId) {
        log.debug("/cancel from own request");
        log.debug("Income userId: {}, requestId: {}", userId, requestId);
        return requestService.cancelOwnRequest(userId, requestId);
    }

    @PatchMapping("/{userId}/events/{eventId}/requests")
    public EventRequestStatusUpdateResult changeRequestsStatus(@PathVariable Long userId,
                                                               @PathVariable Long eventId,
                                                               @RequestBody
                                                                    EventRequestStatusUpdateRequest requestDto) {
        log.debug("/change requests status");
        log.debug("Income userId: {}, eventId: {}, requestDto: {}", userId, eventId, requestDto.toString());
        return requestService.changeRequestsStatus(userId, eventId, requestDto);
    }

    @GetMapping("/{userId}/events/{eventId}/requests")
    public List<ParticipationRequestDto> getEventUserRequests(@PathVariable Long userId,
                                                           @PathVariable Long eventId) {
        log.debug("/get requests by user and event");
        log.debug("Income userId: {}, eventId: {}", userId, eventId);
        return requestService.getRequestsByUserAndEvent(userId, eventId);
    }
}