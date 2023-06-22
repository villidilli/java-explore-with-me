package ru.practicum.testClient;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import ru.practicum.StatsServiceClient;
import ru.practicum.dto.HitRequestDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class TestStatsServiceClient {
    private final StatsServiceClient client;

    @PostMapping("/test/hit")
    public void post(@RequestBody HitRequestDto requestDto) {

        client.saveEndpointHit(requestDto.getApp(), requestDto.getUri(), requestDto.getIp(), LocalDateTime.now());
    }

    @GetMapping("/test/getViewStats")
    public ResponseEntity<List<ViewStatsDto>> get(
                                            @RequestParam("start") String start,
                                            @RequestParam("end") String end,
                                            @RequestParam(name = "uris", required = false) String[] uris,
                                            @RequestParam(name = "unique", defaultValue = "false") Boolean unique) {
        return client.getViewStats(start, end, uris, unique);
    }
}