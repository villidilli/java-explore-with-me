package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query( "SELECT new ru.practicum.ViewStatsDto(h.app, h.uri, COUNT(h.id))" +
            "FROM EndpointHit AS h " +
            "WHERE h.uri IN ?1 AND h.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.id) DESC")
    List<ViewStatsDto> getStats(String[] uris, LocalDateTime start, LocalDateTime end);

    @Query( "SELECT new ru.practicum.ViewStatsDto(h.app, h.uri, COUNT(DISTINCT(h.ip)))" +
            "FROM EndpointHit AS h " +
            "WHERE h.uri IN ?1 AND h.timestamp BETWEEN ?2 AND ?3 " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT(h.ip)) DESC")
    List<ViewStatsDto> getStatsIpUnique(String[] uris, LocalDateTime start, LocalDateTime end);
}