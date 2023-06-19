package ru.practicum;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.context.annotation.PropertySource;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;
}