package ru.practicum.event.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;

import ru.practicum.comment.dto.CommentDto;

import ru.practicum.event.dto.EventFullDto.ParticipationConfig;
import ru.practicum.event.dto.EventFullDto.Statistic;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;

import ru.practicum.user.dto.UserShortDto;

import ru.practicum.utils.ObjectMapperConfig;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
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

    public EventFullDto toFullDto(Event model, Integer confirmedRequests, Integer views, List<CommentDto> comments) {
        EventFullDto dto = new EventFullDto();
        Statistic statistic = new Statistic(confirmedRequests, views);
        Location location = new Location(model.getLocationLat(), model.getLocationLon());
        UserShortDto userShortDto = new UserShortDto(model.getInitiator().getId(), model.getInitiator().getName());
        CategoryDto categoryDto = new CategoryDto(model.getCategory().getId(), model.getCategory().getName());
        ParticipationConfig participation = new ParticipationConfig(model.getRequestModeration(),
                                                                    model.getParticipantLimit(),
                                                                    model.getPaid(),
                                                                    location,
                                                                    model.getEventDate());
        dto.setId(model.getId());
        dto.setTitle(model.getTitle());
        dto.setAnnotation(model.getAnnotation());
        dto.setDescription(model.getDescription());
        dto.setCategory(categoryDto);
        dto.setInitiator(userShortDto);
        dto.setStatistic(statistic);
        dto.setParticipationConfig(participation);
        dto.setCreatedOn(model.getCreatedOn());
        dto.setPublishedOn(model.getPublishedOn());
        dto.setState(model.getState());
        dto.setComments(comments);
        return dto;
    }

    public Event patchMappingToModel(UpdateEventUserRequest updateDto, Optional<Category> category, Event existEvent) {
        ObjectMapper mapper = ObjectMapperConfig.getPatchMapperConfig();
        Map<String, String> updateDtoMap = mapper.convertValue(updateDto, Map.class);
        Map<String, String> existedEventMap = mapper.convertValue(existEvent, Map.class);
        Map<String, String> changedFields = updateDtoMap.entrySet().stream()
                                        .filter(entry -> entry.getValue() != null
                                                && !entry.getKey().equals("location")
                                                && !entry.getKey().equals("category"))
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        mapStateActionToState(changedFields);
        existedEventMap.putAll(changedFields);
        Event updatedEvent = mapper.convertValue(existedEventMap, Event.class);
        if (updatedEvent.getState() == EventState.PUBLISHED) updatedEvent.setPublishedOn(LocalDateTime.now());
        category.ifPresent(updatedEvent::setCategory);
        Location newLocation = updateDto.getLocation();
        if (newLocation != null) {
            updatedEvent.setLocationLat(newLocation.getLat());
            updatedEvent.setLocationLon(newLocation.getLon());
        }
        return updatedEvent;
    }

    public EventShortDto toShortDto(Event model, Integer confirmedRequests, Integer views) {
        log.debug("/event to short dto");
        EventShortDto dto = new EventShortDto();
        dto.setId(model.getId());
        dto.setAnnotation(model.getAnnotation());
        dto.setConfirmedRequests(confirmedRequests);
        dto.setPaid(model.getPaid());
        dto.setViews(views);
        dto.setTitle(model.getTitle());
        dto.setEventDate(model.getEventDate());
        UserShortDto userDto = new UserShortDto();
        userDto.setId(model.getInitiator().getId());
        userDto.setName(model.getInitiator().getName());
        dto.setInitiator(userDto);
        CategoryDto catDto = new CategoryDto();
        catDto.setId(model.getCategory().getId());
        catDto.setName(model.getCategory().getName());
        dto.setCategory(catDto);
        return dto;
    }

    private void mapStateActionToState(Map<String, String> changedFields) {
        String newStateAction = changedFields.get("stateAction");
        if (newStateAction == null) return;
        if (newStateAction.equalsIgnoreCase(StateAction.PUBLISH_EVENT.name())) {
            changedFields.put("state", EventState.PUBLISHED.name());
            changedFields.remove("stateAction");
        }
        if (newStateAction.equalsIgnoreCase(StateAction.REJECT_EVENT.name())) {
            changedFields.put("state", EventState.CANCELED.name());
            changedFields.remove("stateAction");
        }
        if (newStateAction.equalsIgnoreCase(StateAction.SEND_TO_REVIEW.name())) {
            changedFields.put("state", EventState.PENDING.name());
            changedFields.remove("stateAction");
        }
        if (newStateAction.equalsIgnoreCase(StateAction.CANCEL_REVIEW.name())) {
            changedFields.put("state", EventState.CANCELED.name());
            changedFields.remove("stateAction");
        }
    }
}