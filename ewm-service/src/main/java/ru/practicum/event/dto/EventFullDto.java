package ru.practicum.event.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonUnwrapped;

import lombok.*;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.user.dto.UserShortDto;
import ru.practicum.utils.Constant;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class EventFullDto {
    private Long id;
    private String title;
    private String annotation;
    private String description;
    private CategoryDto category;
    private UserShortDto initiator;
    @JsonUnwrapped
    private Statistic statistic;
    @JsonUnwrapped
    private ParticipationConfig participationConfig;
    @JsonFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime createdOn;
    @JsonFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime publishedOn;
    private EventState state;
    private List<CommentDto> comments;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    protected static class Statistic {
        private Integer confirmedRequests;
        private Integer views;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    protected static class ParticipationConfig {
        private Boolean requestModeration;
        private Integer participantLimit;
        private Boolean paid;
        private Location location;
        @JsonFormat(pattern = Constant.dateTimeFormat)
        private LocalDateTime eventDate;
    }
}