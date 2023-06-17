package ru.practicum;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class StatsServiceClient {
    private final WebClient webClient;

    public StatsServiceClient() {
        webClient = WebClient.create("http://localhost:9090");
    }

    public void saveEndpointHit(String app, String uri, String ip, LocalDateTime timestamp) {
        webClient.post()
                .uri("/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(new HitRequestDto(app, uri, ip, timestamp))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    public ResponseEntity<List<ViewStatsDto>> getViewStats(String start, String  end, String[] uris, boolean unique) {
        return webClient.get()
                .uri(uriBuilder  -> uriBuilder
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("unique", unique)
                        .queryParam("uris", uris)
                        .build())
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .toEntityList(ViewStatsDto.class)
                .block();
    }
}