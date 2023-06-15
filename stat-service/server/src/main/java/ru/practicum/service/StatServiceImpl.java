package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.repository.StatRepository;
import ru.practicum.model.EndpointHit;

import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class StatServiceImpl implements StatService {
    private final StatRepository repository;

    @Override
    public void saveHit(EndpointHit endpointHit) {
        log.debug("/saveHit");
        repository.save(endpointHit);
    }

    @Override
    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        log.debug("/getViewStats");
        if (unique) {
            return repository.getStatsIpUnique(uris, start, end);
        }
        return repository.getStats(uris, start, end);
    }
}