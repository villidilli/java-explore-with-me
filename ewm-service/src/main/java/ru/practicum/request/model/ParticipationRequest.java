package ru.practicum.request.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;
import ru.practicum.utils.Constant;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "requests")
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "requester_id")
    private User requester;
    @DateTimeFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime created;
    @Enumerated(EnumType.STRING)
    private ParticipationRequestState status;
}