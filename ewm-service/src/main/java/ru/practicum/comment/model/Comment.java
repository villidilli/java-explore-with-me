package ru.practicum.comment.model;

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
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String text;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commentator_id")
    private User commentator;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
    @DateTimeFormat(pattern = Constant.dateTimeFormat)
    private LocalDateTime timestamp;
}