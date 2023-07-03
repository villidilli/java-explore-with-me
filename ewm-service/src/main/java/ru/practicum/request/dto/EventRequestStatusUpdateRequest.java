package ru.practicum.request.dto;

import lombok.*;
import ru.practicum.request.model.ParticipationRequestState;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventRequestStatusUpdateRequest {
    private List<Long> requestIds;
    private ParticipationRequestState status;
}