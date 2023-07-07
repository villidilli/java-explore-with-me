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
    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE :users IS NULL OR e.initiator.id IN :users " +
                "AND :states IS NULL OR e.state IN :states " +
                "AND :categories IS NULL OR e.category.id IN :categories " +
                "AND e.eventDate BETWEEN COALESCE(:rangeStart, e.eventDate) AND COALESCE(:rangeEnd, e.eventDate)")
    Page<Event> searchEventsAdmin(List<Long> users, List<EventState> states, List<Long> categories,
                                  LocalDateTime rangeStart, LocalDateTime rangeEnd, PageRequest pageRequest);

    Page<Event> findAllByInitiator_Id(Long initiatorId, PageRequest pageRequest);

    @Query("SELECT e " +
            "FROM Event AS e " +
            "WHERE :text IS NULL OR ((LOWER(e.annotation) LIKE LOWER(CONCAT('%', :text, '%'))) " +
                                "OR (LOWER(e.description) LIKE LOWER(CONCAT('%', :text, '%'))) " +
                                "OR (LOWER(e.title) LIKE LOWER(CONCAT('%', :text, '%')))) " +
            "AND (:categories IS NULL OR e.category.id IN :categories) " +
            "AND (:paid IS NULL OR e.paid = :paid) " +
            "AND (e.eventDate BETWEEN :rangeStart AND :rangeEnd) " +
            "AND (e.state = :state)")
    Page<Event> getEventsForPublic(String text, List<Long> categories, Boolean paid, LocalDateTime rangeStart,
                                   LocalDateTime rangeEnd, EventState state, PageRequest request);

    List<Event> findAllByCategory_Id(Long catId);
}