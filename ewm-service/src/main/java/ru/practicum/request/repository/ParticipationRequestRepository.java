package ru.practicum.request.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.practicum.request.model.CountEventRequests;
import ru.practicum.request.model.ParticipationRequestState;
import ru.practicum.request.model.ParticipationRequest;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

    ParticipationRequest findByRequester_IdIsAndEvent_IdIs(Long requesterId, Long eventId);

    Integer countAllByEvent_IdIs(Long eventId);

    Integer countAllByEvent_IdIsAndStatusIs(Long eventId, ParticipationRequestState state);

    Integer countAllByStatusIs(ParticipationRequestState state);

    List<ParticipationRequest> findAllByRequester_Id(Long userId);

    List<ParticipationRequest> findAllByEvent_Id(Long eventId);

    @Query("SELECT new ru.practicum.request.model.CountEventRequests(COUNT(r), r.event.id) " +
            "FROM ParticipationRequest AS r " +
            "WHERE r.event.id IN :eventIds AND r.status = :state " +
            "GROUP BY r.event.id")
    List<CountEventRequests> getCountEventRequests(List<Long> eventIds, ParticipationRequestState state);
}