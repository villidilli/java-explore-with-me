package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query( "SELECT e " +
            "FROM Event AS e " +
            "WHERE (:users is null OR e.initiator.id IN :users) " +
                "AND (:states is null OR e.state IN :states) " +
                "AND (:categories is null OR e.category IN :categories) " +
                "AND ((:rangeStart is null OR :rangeEnd is null) OR e.eventDate BETWEEN :rangeStart AND :rangeEnd)"
    )
    Page<Event> getEvents(Long[] users,
                          EventState[] states,
                          Long[] categories,
                          LocalDateTime rangeStart,
                          LocalDateTime rangeEnd,
                          PageRequest pageRequest);
}

//    private Long id;
//    private String annotation;
//    private Category category;
//    private LocalDateTime createdOn;
//    private String description;
//    private LocalDateTime eventDate;
//    private Float locationLat;
//    private Float locationLon;
//    private Boolean paid;
//    private Integer participantLimit;
//    private LocalDateTime publishedOn;
//    private Boolean requestModeration;
//    private EventState state;
//    private String title;
//    private User initiator;