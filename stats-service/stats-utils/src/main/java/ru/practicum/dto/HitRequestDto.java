package ru.practicum.dto;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

import static ru.practicum.constant.StatsConstant.datetimePattern;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class HitRequestDto {
    private String app;
    private String uri;
    private String ip;
    @JsonFormat(pattern = datetimePattern)
    private LocalDateTime timestamp;
}