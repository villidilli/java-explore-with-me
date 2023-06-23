package ru.practicum.request.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.utils.Constant;
import ru.practicum.utils.Status;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ParticipationRequestDto {
    private Long id;
    @DateTimeFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime created;
    @NotBlank
    private Long event;
    @NotBlank
    private Long requester;
    private Status status;

}