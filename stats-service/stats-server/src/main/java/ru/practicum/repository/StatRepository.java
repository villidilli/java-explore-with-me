package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.practicum.model.EndpointHit;
import ru.practicum.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatRepository extends JpaRepository<EndpointHit, Long> {

    @Query ("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT(h.id)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.uri IN :uris AND h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.id) DESC")
    List<ViewStats> getStats(String[] uris, LocalDateTime start, LocalDateTime end);

    @Query ("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT(h.id)) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.id) DESC")
    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end);

    @Query ("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT(DISTINCT(h.ip))) " +
            "FROM EndpointHit AS h " +
            "WHERE h.uri IN :uris AND h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT(h.ip)) DESC")
    List<ViewStats> getStatsIpUnique(String[] uris, LocalDateTime start, LocalDateTime end);

    @Query ("SELECT new ru.practicum.model.ViewStats(h.app, h.uri, COUNT(DISTINCT(h.ip))) " +
            "FROM EndpointHit AS h " +
            "WHERE h.timestamp BETWEEN :start AND :end " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT(h.ip)) DESC")
    List<ViewStats> getStatsIpUnique(LocalDateTime start, LocalDateTime end);
}