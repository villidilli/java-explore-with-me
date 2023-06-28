package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;

import java.time.LocalDateTime;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query( "SELECT e " +
            "FROM Event AS e " +
            "WHERE :users is null OR e.initiator.id IN :users " +
                "AND :states is null OR e.state IN :states " +
                "AND :categories is null OR e.category.id IN :categories " +
                "AND e.eventDate BETWEEN COALESCE(:rangeStart, e.eventDate) AND COALESCE(:rangeEnd, e.eventDate)"
    )
    Page<Event> getEvents(List<Long> users, List<EventState> states, List<Long> categories,
                          LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest);

    Page<Event> findAllByInitiator_Id(Long initiatorId, PageRequest pageRequest);
}