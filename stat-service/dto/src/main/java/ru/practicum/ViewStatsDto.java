package ru.practicum;

import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ViewStatsDto {
    private String app;
    private String uri;
    private Long hits;

    //JPQL не видит конструктор от @AllArgsConstructor
    public ViewStatsDto(String app, String uri, Long hits) {
        this.app = app;
        this.uri = uri;
        this.hits = hits;
    }
}