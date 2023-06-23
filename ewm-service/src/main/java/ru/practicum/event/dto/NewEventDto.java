package ru.practicum.event.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.event.model.Location;
import ru.practicum.utils.Constant;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewEventDto {
    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;
    @NotBlank
    private Long category;
    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;
    @NotBlank
    @DateTimeFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime eventDate;
    @NotBlank
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @NotBlank
    @Length(min = 3, max = 120)
    private String title;
}