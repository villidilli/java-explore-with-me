package ru.practicum.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import ru.practicum.constant.StatsConstant;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HitRequestDto {
    private String app;
    private String uri;
    private String ip;
    @JsonFormat(pattern = StatsConstant.datetimePattern)
    private LocalDateTime timestamp;
}