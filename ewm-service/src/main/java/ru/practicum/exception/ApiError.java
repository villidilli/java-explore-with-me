package ru.practicum.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.utils.Constant;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ApiError {
    private String status;
    private String reason;
    private String message;
    @JsonFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime timestamp;
}