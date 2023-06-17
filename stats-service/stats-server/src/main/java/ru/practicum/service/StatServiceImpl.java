package ru.practicum.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.model.ModelToDtoMapper;
import ru.practicum.model.ViewStats;
import ru.practicum.repository.StatRepository;
import ru.practicum.model.EndpointHit;

import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(isolation = Isolation.REPEATABLE_READ)
public class StatServiceImpl implements StatService {
    private final StatRepository repository;

    @Override
    public void saveHit(EndpointHit endpointHit) {
        log.debug("/saveHit");
        Long id = repository.save(endpointHit).getId();
        log.debug("assigned id: {}", id);
    }

    @Override
    public List<ViewStatsDto> getViewStats(LocalDateTime start, LocalDateTime end, String[] uris, Boolean unique) {
        log.debug("/getViewStats");
        List<ViewStats> resultFromRepo;

        if (uris == null) {
            if (unique) {
                log.debug("/getStatsIpUnique, uris = null");
                resultFromRepo = repository.getStatsIpUnique(start, end);
            } else {
                log.debug("/getStats, uris = null");
                resultFromRepo = repository.getStats(start, end);
            }
        } else {
            if (unique) {
                log.debug("/getStatsIpUnique, uris have");
                resultFromRepo = repository.getStatsIpUnique(uris, start, end);
            } else {
                log.debug("/getStats, uris have");
                resultFromRepo = repository.getStats(uris, start, end);
            }
        }

        return resultFromRepo.stream()
                .map(ModelToDtoMapper::toViewStatsDto)
                .collect(Collectors.toList());
    }
}