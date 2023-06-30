package ru.practicum.request.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.event.model.Event;
import ru.practicum.request.model.ParticipationRequestState;
import ru.practicum.request.model.ParticipationRequest;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class ParticipationRequestMapper {

    public ParticipationRequest toModel(User user, Event event) {
        ParticipationRequest model = new ParticipationRequest();
        model.setRequester(user);
        model.setEvent(event);
        model.setCreated(LocalDateTime.now());
        model.setStatus(ParticipationRequestState.PENDING);
        return model;
    }

    public ParticipationRequestDto toDto(ParticipationRequest model) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setCreated(model.getCreated());
        dto.setStatus(model.getStatus());
        dto.setId(model.getId());
        dto.setEvent(model.getEvent().getId());
        dto.setRequester(model.getRequester().getId());
        return dto;
    }
}