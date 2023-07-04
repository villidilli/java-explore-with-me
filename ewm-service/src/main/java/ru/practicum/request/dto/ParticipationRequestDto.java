package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.request.model.ParticipationRequestState;
import ru.practicum.utils.Constant;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParticipationRequestDto {
    private Long id;
    @JsonFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime created;
    private Long event;
    private Long requester;
    private ParticipationRequestState status;
}