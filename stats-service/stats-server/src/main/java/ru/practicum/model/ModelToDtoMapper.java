package ru.practicum.model;

import lombok.experimental.UtilityClass;

import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.ViewStatsDto;

@UtilityClass
public class ModelToDtoMapper {

    public EndpointHit toHitModel(HitRequestDto requestDto) {
        EndpointHit model = new EndpointHit();
        model.setApp(requestDto.getApp());
        model.setUri(requestDto.getUri());
        model.setIp(requestDto.getIp());
        model.setTimestamp(requestDto.getTimestamp());
        return model;
    }

    public ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        ViewStatsDto dto = new ViewStatsDto();
        dto.setApp(viewStats.getApp());
        dto.setUri(viewStats.getUri());
        dto.setHits(viewStats.getHits());
        return dto;
    }
}