package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.request.model.ParticipationRequest;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    ParticipationRequest countByRequester_IdIsAndEvent_IdIs(Long requesterId, Long eventId);

    Integer countAllByEvent_IdIs(Long eventId);
}