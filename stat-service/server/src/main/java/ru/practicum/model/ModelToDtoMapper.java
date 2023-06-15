package ru.practicum.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.HitRequestDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ModelToDtoMapper {

    public static EndpointHit toModel(HitRequestDto requestDto) {
        EndpointHit model = new EndpointHit();
        model.setApp(requestDto.getApp());
        model.setUri(requestDto.getUri());
        model.setIp(requestDto.getIp());
        model.setTimestamp(requestDto.getTimestamp());
        return model;
    }
}