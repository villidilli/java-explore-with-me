package ru.practicum.request.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.request.model.PapticipationRequestState;
import ru.practicum.utils.Constant;

import javax.validation.constraints.NotBlank;
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
    @NotBlank
    private Long event;
    @NotBlank
    private Long requester;
    private PapticipationRequestState status;

}