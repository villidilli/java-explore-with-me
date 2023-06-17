package ru.practicum;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.practicum.model.EndpointHit;
import ru.practicum.repository.StatRepository;
import ru.practicum.service.StatServiceImpl;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StatServiceImplTest {
    @Mock
    private StatRepository repository;
    @InjectMocks
    private StatServiceImpl service;

    private EndpointHit endpointHit;
    private LocalDateTime date1;

    @BeforeEach
    public void beforeEach() {
        date1 = LocalDateTime.of(2023, 06, 14, 0, 0);

        endpointHit = new EndpointHit();
        endpointHit.setId(1L);
        endpointHit.setApp("ewm-main-service");
        endpointHit.setUri("/events/1");
        endpointHit.setIp("192.163.0.1");
        endpointHit.setTimestamp(date1);
    }

    @Test
    public void createEndpointHit_thenReturnStatus201withoutBody() {
        when(repository.save(any())).thenReturn(endpointHit);

        service.saveHit(endpointHit);

        verify(repository, times(1)).save(any());
    }
}
