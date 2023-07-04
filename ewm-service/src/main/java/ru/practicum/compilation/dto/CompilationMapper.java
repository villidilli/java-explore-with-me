package ru.practicum.compilation.dto;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.experimental.UtilityClass;

import ru.practicum.compilation.model.Compilation;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.utils.ObjectMapperConfig;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@UtilityClass
public class CompilationMapper {

    public Compilation toModel(NewCompilationDto dto, List<Event> events) {
        Compilation model = new Compilation();
        model.setTitle(dto.getTitle());
        model.setPinned(dto.getPinned());
        model.setEvents(events);
        return model;
    }

    public CompilationDto toDto(Compilation model, List<EventShortDto> eventShortDtos) {
        CompilationDto dto = new CompilationDto();
        dto.setId(model.getId());
        dto.setTitle(model.getTitle());
        dto.setPinned(model.getPinned());
        dto.setEvents(eventShortDtos);
        return dto;
    }

    public Compilation patchMappingToModel(Compilation existedCompilation,
                                           UpdateCompilationRequest updateDto,
                                           List<Event> events) {
        ObjectMapper mapper = ObjectMapperConfig.getPatchMapperConfig();
        Map<String, String> updateDtoMap = mapper.convertValue(updateDto, Map.class);
        Map<String, String> existedCompMap = mapper.convertValue(existedCompilation, Map.class);
        Map<String, String> changedFields = updateDtoMap.entrySet().stream()
                .filter(entry -> entry.getValue() != null
                        && !entry.getKey().equals("events"))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
        existedCompMap.putAll(changedFields);
        Compilation updatedComp = mapper.convertValue(existedCompMap, Compilation.class);
        updatedComp.setEvents(events);
        return updatedComp;
    }
}