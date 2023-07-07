package ru.practicum.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import ru.practicum.utils.Constant;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentDto {
    private Long id;
    private String text;
    private Long commentatorId;
    private Long eventId;
    @JsonFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime timestamp;
}