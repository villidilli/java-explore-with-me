package ru.practicum;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@FeignClient(value = "stats-server", url = "localhost:9090/")
public interface StatServiceClient {

    @PostMapping("/hit")
    void saveEndpointHit();
}