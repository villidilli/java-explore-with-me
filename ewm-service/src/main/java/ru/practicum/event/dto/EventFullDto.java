package ru.practicum.event.dto;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.utils.Status;
import ru.practicum.event.model.Location;
import ru.practicum.utils.Constant;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryResponseDto category;
    private Integer confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    @DateTimeFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    @DateTimeFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private Status state;
    private String title;
    private Integer views;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class UserShortDto {
        private Long id;
        private String name;
    }
}