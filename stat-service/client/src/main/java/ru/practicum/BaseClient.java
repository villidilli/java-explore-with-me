//package ru.practicum.client;
//
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.MediaType;
//import org.springframework.web.client.RestTemplate;
//import org.springframework.web.reactive.function.client.WebClient;
//import ru.practicum.statservice.dto.HitRequestDto;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.function.Consumer;
//
//public class BaseClient {
//    protected final WebClient webClient;
//
//    public BaseClient() {
//        webClient = WebClient.builder()
//                .baseUrl("http://localhost:9090")
//                .defaultHeaders(httpHeaders -> createHeaders())
//                .build();
//    }
//
//    public void saveHit(String app, String uri, String ip, LocalDateTime timestamp) {
//        HitRequestDto requestDto = new HitRequestDto();
//        requestDto.setApp(app);
//        requestDto.setUri(uri);
//        requestDto.setIp(ip);
//        requestDto.setTimestamp(timestamp);
//
//        webClient.post()
//                .uri("/hit")
//                .bodyValue(requestDto)
//                .attributes()
//    }
//
//
//    private HttpHeaders createHeaders() {
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
//        return headers;
//    }
//
//    private
//
//}
