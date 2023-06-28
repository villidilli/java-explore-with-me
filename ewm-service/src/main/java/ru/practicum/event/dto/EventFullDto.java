package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.event.model.Location;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.utils.Constant;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventFullDto {
    private Long id;
    private String annotation;
    private CategoryDto category;
    private Integer confirmedRequests;
    @JsonFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime createdOn;
    private String description;
    @JsonFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    @JsonFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private EventState state;
    private String title;
    private Integer views;


//    @Getter
//    @Setter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @ToString
//    public static class UserShortDto {
//        private Long id;
//        private String name;
//    }
}