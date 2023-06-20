package ru.practicum.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query ("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT(h.id) ) " +
            "FROM EndpointHit AS h " +
            "WHERE h.uri IN :uris AND h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri")
    List<ViewStats> getStats(String[] uris, LocalDateTime start, LocalDateTime end, Sort sort);

    @Query ("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT(h.id)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri")
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, Sort sort);

    @Query ("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT(DISTINCT(h.ip))) " +
            "FROM EndpointHit AS h " +
            "WHERE h.uri IN :uris AND h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri")
    List<ViewStats> getStatsIpUnique(String[] uris, LocalDateTime start, LocalDateTime end, Sort sort);

    @Query ("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT(DISTINCT(h.ip))) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri")
    List<ViewStats> getStatsIpUnique(LocalDateTime start, LocalDateTime end, Sort sort);
}