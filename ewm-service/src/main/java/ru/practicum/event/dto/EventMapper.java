package ru.practicum.event.dto;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.model.Location;
import ru.practicum.utils.Constant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@UtilityClass
@Slf4j
public class EventMapper {

    public Event toModel(NewEventDto dto) {
        log.debug("/event dto to model");
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
        log.debug("/event to dto");
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

    public Event patchMappingToModel(UpdateEventUserRequest updateDto, Optional<Category> category, Event existedEvent) {
        log.debug("/patch event from dto");
        ObjectMapper mapper = getPatchEventMapper();
        Map<String, String > updateDtoMap = mapper.convertValue(updateDto, Map.class);
        Map<String, String > existedEventMap = mapper.convertValue(existedEvent, Map.class);
        Map<String, String > changedFields = updateDtoMap.entrySet().stream()
                                        .filter(entry -> entry.getValue() != null
                                                && !entry.getKey().equals("location")
                                                && !entry.getKey().equals("category"))
                                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        String newStateAction = changedFields.get("stateAction");
        if (newStateAction.equalsIgnoreCase(StateAction.PUBLISH_EVENT.name())) {
            changedFields.put("state", EventState.PUBLISHED.name());
            changedFields.remove("stateAction");
        }
        if (newStateAction.equalsIgnoreCase(StateAction.REJECT_EVENT.name())) {
            changedFields.put("state", EventState.CANCELED.name());
            changedFields.remove("stateAction");
        }


        existedEventMap.putAll(changedFields);
        Event updatedEvent = mapper.convertValue(existedEventMap, Event.class);
        if (updatedEvent.getState() == EventState.PUBLISHED) updatedEvent.setPublishedOn(LocalDateTime.now());
        if (category != null) updatedEvent.setCategory(category);
        category.ifPresent(updatedEvent::setCategory);
        Location newLocation = updateDto.getLocation();
        if (newLocation != null) {
            updatedEvent.setLocationLat(newLocation.getLat());
            updatedEvent.setLocationLon(newLocation.getLon());
        }
        return updatedEvent;
    }

    private ObjectMapper getPatchEventMapper() {
        log.debug("/get patch event mapper");
        JavaTimeModule timeModule = new JavaTimeModule();
        timeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(DateTimeFormatter.ofPattern(Constant.dateTimeFormat)));
        return JsonMapper.builder()
                .addModule(timeModule)
                .configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false)
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .build();
    }
}

//    private String annotation;
//    private Long category;
//    private String description;
//    private LocalDateTime eventDate;
//    private Location location;
//    private Boolean paid;
//    private Integer participantLimit;
//    private Boolean requestModeration;
//    private StateAction stateAction;
//    private String title;