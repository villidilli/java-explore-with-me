package ru.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import ru.practicum.model.EndpointHit;
import ru.practicum.model.ModelToDtoMapper;
import ru.practicum.model.ViewStats;

import ru.practicum.repository.StatRepository;

import ru.practicum.service.StatServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatServiceImplTest {
    @Mock
    private StatRepository repository;
    @Mock
    private ModelToDtoMapper mapper;
    @InjectMocks
    private StatServiceImpl service;

    private EndpointHit endpointHit;
    private LocalDateTime date1;
    private ViewStatsDto viewStatsDto;
    private ViewStats viewStats;
    private LocalDateTime start;
    private LocalDateTime end;

    @BeforeEach
    public void beforeEach() {
        date1 = LocalDateTime.of(2023, 06, 14, 0, 0);

        endpointHit = new EndpointHit();
        endpointHit.setId(1L);
        endpointHit.setApp("ewm-main-service");
        endpointHit.setUri("/events/1");
        endpointHit.setIp("192.163.0.1");
        endpointHit.setTimestamp(date1);

        viewStats = new ViewStats();
        viewStats.setApp("ewm-main-service");
        viewStats.setUri("/events/1");
        viewStats.setHits(5L);

        viewStatsDto = ModelToDtoMapper.toViewStatsDto(viewStats);

        start = LocalDateTime.of(2020, 1, 1, 1, 1);
        end = LocalDateTime.of(2025, 1, 1, 1, 1);
    }

    @Test
    public void createEndpointHit() {
        when(repository.save(any())).thenReturn(endpointHit);

        service.saveHit(endpointHit);

        verify(repository, times(1)).save(any());
    }

    @Test
    public void getStats_whenUniqueTrueUrisNull_thenReturnListDto() {
        List<ViewStats> expected = List.of(viewStats);
        when(repository.getStatsIpUnique(any(), any())).thenReturn(expected);

        List<ViewStatsDto> actual = service.getViewStats(start, end, null, true);

        assertNotNull(actual);
        assertNotNull(actual.get(0).getHits());
        assertNotNull(actual.get(0).getApp());
        assertNotNull(actual.get(0).getUri());

        verify(repository, times(1)).getStatsIpUnique(any(), any());
    }

    @Test
    public void getStats_whenUniqueFalseUrisNull_thenReturnListDto() {
        List<ViewStats> expected = List.of(viewStats);
        when(repository.getStats(any(), any())).thenReturn(expected);

        List<ViewStatsDto> actual = service.getViewStats(start, end, null, false);

        assertNotNull(actual);
        assertNotNull(actual.get(0).getHits());
        assertNotNull(actual.get(0).getApp());
        assertNotNull(actual.get(0).getUri());

        verify(repository, times(1)).getStats(any(), any());
    }

    @Test
    public void getStats_whenUniqueTrueUrisNotNull_thenReturnListDto() {
        List<ViewStats> expected = List.of(viewStats);
        when(repository.getStatsIpUnique(any(), any(), any())).thenReturn(expected);

        List<ViewStatsDto> actual = service.getViewStats(start, end, new String[0], true);

        assertNotNull(actual);
        assertNotNull(actual.get(0).getHits());
        assertNotNull(actual.get(0).getApp());
        assertNotNull(actual.get(0).getUri());

        verify(repository, times(1)).getStatsIpUnique(any(), any(), any());
    }

    @Test
    public void getStats_whenUniqueFalseUrisNotNull_thenReturnListDto() {
        List<ViewStats> expected = List.of(viewStats);
        when(repository.getStats(any(), any(), any())).thenReturn(expected);

        List<ViewStatsDto> actual = service.getViewStats(start, end, new String[0], false);

        assertNotNull(actual);
        assertNotNull(actual.get(0).getHits());
        assertNotNull(actual.get(0).getApp());
        assertNotNull(actual.get(0).getUri());

        verify(repository, times(1)).getStats(any(), any(), any());
    }
}