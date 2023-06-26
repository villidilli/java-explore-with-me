package ru.practicum.event.model;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.category.model.Category;
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
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;
    @DateTimeFormat(pattern = Constant.dateTimeFormat)
    @Column(name = "created_on")
    private LocalDateTime createdOn;
    private String description;
    @DateTimeFormat(pattern = Constant.dateTimeFormat)
    @Column(name = "event_date")
    private LocalDateTime eventDate;
    @Column(name = "location_lat")
    private Float locationLat;
    @Column(name = "location_lon")
    private Float locationLon;
    private Boolean paid;
    @Column(name = "participant_limit")
    private Integer participantLimit;
    @DateTimeFormat(pattern = Constant.dateTimeFormat)
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Column(name = "request_moderation")
    private Boolean requestModeration;
    @Enumerated(EnumType.STRING)
    private EventState state;
    private String title;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User initiator;
}