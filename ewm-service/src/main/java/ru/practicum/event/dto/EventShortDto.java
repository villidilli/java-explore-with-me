package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.utils.Constant;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventShortDto {
    @NotNull
    private Long id;
    @NotBlank
    private String annotation;
    @NotNull
    private CategoryDto category;
    private Integer confirmedRequests;
    @NotNull
    @JsonFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime eventDate;
    @NotNull
    private UserShortDto initiator;
    @NotNull
    private Boolean paid;
    @NotBlank
    private String title;
    private Integer views;
}