package ru.practicum.service;

import ru.practicum.model.EndpointHit;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatService {

    void saveHit(EndpointHit endpointHit);

    List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique);
}