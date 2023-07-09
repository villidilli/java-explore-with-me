package ru.practicum.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import org.hibernate.validator.constraints.Length;
import ru.practicum.utils.Constant;
import ru.practicum.validation.ValidateMarker;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CommentDto {
    @Null(groups = {ValidateMarker.OnCreate.class, ValidateMarker.OnUpdate.class})
    private Long id;
    @Length(min = 1, max = 7000)
    @NotBlank
    private String text;
    @Null(groups = {ValidateMarker.OnCreate.class, ValidateMarker.OnUpdate.class})
    private Long commentatorId;
    @Null(groups = {ValidateMarker.OnCreate.class, ValidateMarker.OnUpdate.class})
    private Long eventId;
    @Null(groups = {ValidateMarker.OnCreate.class, ValidateMarker.OnUpdate.class})
    @JsonFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime timestamp;
}