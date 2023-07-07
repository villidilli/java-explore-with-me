package ru.practicum.request.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CountEventRequests {
    private Long requests;
    private Long eventId;

    public CountEventRequests(Long requests, Long eventId) {
        this.requests = requests;
        this.eventId = eventId;
    }
}