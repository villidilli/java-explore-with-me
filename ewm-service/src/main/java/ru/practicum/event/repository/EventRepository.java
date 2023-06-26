package ru.practicum.event.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query("SELECT e" +
            "FROM events")
    Page<Event> getEvents()
}