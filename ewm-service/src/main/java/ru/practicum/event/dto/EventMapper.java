package ru.practicum.event.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

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
        CategoryDto categoryDto = new CategoryDto();
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

    public Event patchEventFromDto(UpdateEventUserRequest updateDto, Optional<Category> category, Event existedEvent) {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, String> updateMapWithNullFields = mapper.convertValue(updateDto, Map.class);
        Map<String, String> existedMap = mapper.convertValue(existedEvent, Map.class);
        Map<String, String> updateMapWithoutNull = new HashMap<>();
        updateMapWithNullFields.entrySet().forEach(new Consumer<Map.Entry<String, String>>() {
            @Override
            public void accept(Map.Entry<String, String> entry) {
                if (entry.getValue() != null)
            }
        });


    }
}

//    private Long id;
//    private String annotation;
//    private Category category;
//    private LocalDateTime createdOn;
//    private String description;
//    private LocalDateTime eventDate;
//    private Float locationLat;
//    private Float locationLon;
//    private Boolean paid;
//    private Integer participantLimit;
//    private LocalDateTime publishedOn;
//    private Boolean requestModeration;
//    private EventState state;
//    private String title;
//    private User initiator;