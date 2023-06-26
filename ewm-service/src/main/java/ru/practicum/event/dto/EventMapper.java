package ru.practicum.event.dto;

import lombok.experimental.UtilityClass;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;

import java.time.LocalDateTime;

@UtilityClass
public class EventMapper {

    public Event toModel(NewEventDto dto) {
        Event model = new Event();
        model.setAnnotation(dto.getAnnotation());
        Category category = new Category();
        category.setId(dto.getCategory());
        model.setCategory(category);
        model.setEventDate(dto.getEventDate());
        model.setCreatedOn(LocalDateTime.now());
        model.setDescription(dto.getDescription());
        model.setEventDate(dto.getEventDate());
        model.setLocationLat(dto.getLocation().getLat());
        model.setLocationLon(dto.getLocation().getLon());
        model.setPaid(dto.getPaid());
        model.setParticipantLimit(dto.getParticipantLimit());
        model.setRequestModeration(dto.getRequestModeration());
        model.setState(EventState.PENDING);
        model.setTitle(dto.getTitle());
        return model;
    }

    public EventFullDto toDto(Event model, Integer confirmedRequests, Integer views) {
        EventFullDto dto = new EventFullDto();
        dto.setId(model.getId());
        dto.setAnnotation(model.getAnnotation());
        CategoryResponseDto categoryDto = new CategoryResponseDto();
        categoryDto.setId(model.getCategory().getId());
        categoryDto.setName(model.getCategory().getName());
        dto.setCategory(categoryDto);
        dto.setConfirmedRequests(confirmedRequests);
        dto.setCreatedOn(model.getCreatedOn());
        dto.setDescription(model.getDescription());
        dto.setEventDate(model.getEventDate());
        EventFullDto.UserShortDto userShortDto = new EventFullDto.UserShortDto();
        userShortDto.setId(model.getInitiator().getId());
        userShortDto.setName(model.getInitiator().getName());
        dto.setInitiator(userShortDto);
        Location location = new Location();
        location.setLat(model.getLocationLat());
        location.setLon(model.getLocationLon());
        dto.setLocation(location);
        dto.setPaid(model.getPaid());
        dto.setParticipantLimit(model.getParticipantLimit());
        dto.setPublishedOn(model.getPublishedOn());
        dto.setRequestModeration(model.getRequestModeration());
        dto.setState(model.getState());
        dto.setTitle(model.getTitle());
        dto.setViews(views);
        return dto;
    }
}