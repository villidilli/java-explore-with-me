package ru.practicum.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import ru.practicum.HitRequestDto;
import ru.practicum.ViewStatsDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModelToDtoMapper {

    public static EndpointHit toHitModel(HitRequestDto requestDto) {
        EndpointHit model = new EndpointHit();
        model.setApp(requestDto.getApp());
        model.setUri(requestDto.getUri());
        model.setIp(requestDto.getIp());
        model.setTimestamp(requestDto.getTimestamp());
        return model;
    }

    public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
        ViewStatsDto dto = new ViewStatsDto();
        dto.setApp(viewStats.getApp());
        dto.setUri(viewStats.getUri());
        dto.setHits(viewStats.getHits());
        return dto;
    }
}