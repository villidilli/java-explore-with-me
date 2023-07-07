package ru.practicum.event.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.StatsServiceClient;
import ru.practicum.category.model.Category;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;

import ru.practicum.dto.ViewStatsDto;

import ru.practicum.event.dto.*;
import ru.practicum.event.model.Event;
import ru.practicum.event.model.EventState;
import ru.practicum.event.repository.EventRepository;

import ru.practicum.exception.FieldConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidateException;

import ru.practicum.request.model.CountEventRequests;
import ru.practicum.request.model.ParticipationRequestState;
import ru.practicum.request.repository.ParticipationRequestRepository;

import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import ru.practicum.utils.Constant;
import ru.practicum.utils.EventViewSort;
import ru.practicum.utils.PageConfig;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final ParticipationRequestRepository requestRepository;
    private final CategoryRepository categoryRepository;
    private final CommentRepository commentRepository;
    private final StatsServiceClient statsClient;

    @Override
    @Transactional
    public EventFullDto createEvent(Long userId, NewEventDto eventRequestDto) {
        log.debug("/create event");
        User existedUser = getExistedUser(userId);
        Category existedCategory = getExistedCategory(eventRequestDto.getCategory());
        Event eventToSave = EventMapper.toModel(eventRequestDto);
        eventToSave.setInitiator(existedUser);
        eventToSave.setCategory(existedCategory);
        Event savedEvent = eventRepository.save(eventToSave);
        log.debug("Saved Event id: {}", savedEvent.getId());
        return EventMapper.toFullDto(savedEvent,0, 0, getComments(savedEvent.getId()));
    }

    @Override
    @Transactional
    public EventFullDto updateEventAdmin(Long eventId, UpdateEventUserRequest updateEventDto) {
        log.debug("/update event admin");
        Event existedEvent = getExistedEvent(eventId);
        checkStateActionConstraint(updateEventDto, existedEvent);
        checkPublishedOnConstraint(updateEventDto, existedEvent);
        Event updatedEvent = EventMapper.patchMappingToModel(updateEventDto,
                                                             getCategoryForPatch(updateEventDto),
                                                             existedEvent);
        Integer confirmedRequests = requestRepository.countAllByStatusIs(ParticipationRequestState.CONFIRMED);
        Event savedEvent = eventRepository.save(updatedEvent);
        return EventMapper.toFullDto(savedEvent, confirmedRequests, getCountViews(eventId), getComments(eventId));
    }

    @Override
    @Transactional
    public EventFullDto updateEventUser(Long userId, Long eventId, UpdateEventUserRequest updateEventDto) {
        log.debug("/update event user");
        getExistedUser(userId);
        Event existedEvent = getExistedEvent(eventId);
        checkConstraintState(existedEvent);
        Event updatedEvent = EventMapper.patchMappingToModel(updateEventDto,
                                                             getCategoryForPatch(updateEventDto),
                                                             existedEvent);
        Integer confirmedRequests = requestRepository.countAllByStatusIs(ParticipationRequestState.CONFIRMED);
        Event savedEvent = eventRepository.save(updatedEvent);
        return EventMapper.toFullDto(savedEvent, confirmedRequests, getCountViews(eventId), getComments(eventId));
    }

    @Override
    public List<EventShortDto> getEventsByUser(Long userId, Integer from, Integer size) {
        log.debug("/get events by user");
        getExistedUser(userId);
        List<Event> searchedEvents =
                        eventRepository.findAllByInitiator_Id(userId, new PageConfig(from, size, Sort.unsorted()))
                            .stream()
                            .collect(Collectors.toList());
        Map<Long, Long> eventIdConfirmedRequestsMap = getConfirmedRequests(searchedEvents);
        Map<Long, Long> eventIdCountHits = getViews(searchedEvents);
        return setRequestsAndViewsShortDto(searchedEvents, eventIdConfirmedRequestsMap, eventIdCountHits);
    }

    @Override
    public EventFullDto getEventByUser(Long userId, Long eventId) {
        log.debug("/get event by user");
        getExistedUser(userId);
        Event existedEvent = getExistedEvent(eventId);
        return EventMapper.toFullDto(existedEvent,
                                     requestRepository.countAllByEvent_IdIs(eventId),
                                     getCountViews(eventId),
                                     getComments(eventId));
    }

    @Override
    public EventFullDto getEventById(Long eventId, HttpServletRequest request) {
        log.debug("/get event by id");
        Event existedEvent = getExistedEvent(eventId);
        checkConstraintNotPublished(existedEvent);
        Integer confirmedRequests = requestRepository.countAllByEvent_IdIs(eventId);
        Integer views = getCountViews(eventId);
        saveEndpointHit(Constant.mainAppName, request);
        return EventMapper.toFullDto(existedEvent, confirmedRequests, views, getComments(eventId));
    }

    @Override
    @Transactional
    public List<EventShortDto> publicSearchEvents(String text, List<Long> categories, Boolean paid,
                                                  LocalDateTime rangeStart, LocalDateTime rangeEnd,
                                                  EventViewSort sort, Boolean onlyAvailable, Integer from, Integer size,
                                                  HttpServletRequest request) {
        log.debug("/get events for public");
        checkConstraintStartEndRange(getStartDate(rangeStart), rangeEnd);
        LocalDateTime startDate = getStartDate(rangeStart);
        List<Event> searchedEvents = eventRepository.getEventsForPublic(text,
                                                                        categories,
                                                                        paid,
                                                                        startDate,
                                                                        rangeEnd,
                                                                        EventState.PUBLISHED,
                                                                        new PageConfig(from, size, Sort.unsorted()))
                .stream()
                .collect(Collectors.toList());
        Map<Long, Long> eventIdConfirmedRequestsMap = getConfirmedRequests(searchedEvents);
        checkOnlyAvailableParam(onlyAvailable, searchedEvents, eventIdConfirmedRequestsMap);
        Map<Long, Long> eventIdCountHits = getViews(searchedEvents);
        List<EventShortDto> resultList = setRequestsAndViewsShortDto(
                                                        searchedEvents, eventIdConfirmedRequestsMap, eventIdCountHits);

        statsClient.saveEndpointHit(
                Constant.mainAppName, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
        return resultList.stream()
                .sorted(getSort(sort))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventFullDto> searchEventsAdmin(List<Long> users,
                                                List<EventState> states,
                                                List<Long> categories,
                                                LocalDateTime rangeStart,
                                                LocalDateTime rangeEnd,
                                                Integer from,
                                                Integer size) {
        log.debug("/search events admin api");
        PageRequest pageRequest = new PageConfig(from, size, Sort.unsorted());
        List<Event> searchedEvents =
                eventRepository.searchEventsAdmin(users, states, categories, rangeStart, rangeEnd, pageRequest)
                        .stream()
                        .collect(Collectors.toList());
        Map<Long, Long> eventIdConfirmedRequestsMap = getConfirmedRequests(searchedEvents);
        Map<Long, Long> eventIdCountHits = getViews(searchedEvents);
        Map<Long, List<CommentDto>> eventIdListComments = getCommentsForSearch(searchedEvents);
        return setRequestsViewsCommentsToFullDto(searchedEvents,
                                                 eventIdConfirmedRequestsMap,
                                                 eventIdCountHits,
                                                 eventIdListComments);
    }

    private Map<Long, List<CommentDto>> getCommentsForSearch(List<Event> searchedEvents) {
        Map<Long, List<CommentDto>> eventIdComments = searchedEvents.stream()
                .collect(Collectors.toMap(Event::getId, list -> Collections.emptyList()));
        List<Comment> allComments = commentRepository.findAllById(eventIdComments.keySet());
        allComments.forEach(comment -> {
           List<CommentDto> eventComments = eventIdComments.getOrDefault(comment.getEvent().getId(),
                                                                        new ArrayList<>());
           if (comment != null) {
               eventComments.add(CommentMapper.toDto(comment));
           }
           eventIdComments.put(comment.getEvent().getId(), eventComments);
        });
        return eventIdComments;
    }

    private void checkOnlyAvailableParam(Boolean onlyAvailable, List<Event> searchedEvents,
                                                                        Map<Long, Long> eventIdConfirmedRequestsMap) {
        if (onlyAvailable) {
            searchedEvents.iterator().forEachRemaining(event -> {
                Long confirmedRequests = eventIdConfirmedRequestsMap.get(event.getId());
                if (confirmedRequests >= event.getParticipantLimit()) searchedEvents.remove(event);
            });
        }
    }

    private void checkConstraintStartEndRange(LocalDateTime start, LocalDateTime end) throws ValidateException {
        if (start.isAfter(end)) throw new ValidateException("EndTime must be after StartTime");
    }

    private void checkPublishedOnConstraint(UpdateEventUserRequest updateEventDto, Event existedEvent)
                                                                                            throws ValidateException {
        LocalDateTime actualPublishedOn = existedEvent.getPublishedOn();
        if (updateEventDto.getEventDate() == null || existedEvent.getPublishedOn() == null) return;
        if (!updateEventDto.getEventDate().isAfter(actualPublishedOn.plusHours(1)))
            throw new ValidateException("Event date must be later then publication date on 1 hour");
    }

    private void checkStateActionConstraint(UpdateEventUserRequest updateEventDto, Event existedEvent)
                                                                                        throws FieldConflictException {
        StateAction newStateAction = updateEventDto.getStateAction();
        if (newStateAction == null) return;
        EventState actualState = existedEvent.getState();
        if (newStateAction.equals(StateAction.PUBLISH_EVENT) && actualState.equals(EventState.PUBLISHED)) {
            throw new FieldConflictException("Event already PUBLISHED");
        }
        if (newStateAction.equals(StateAction.PUBLISH_EVENT) && actualState.equals(EventState.CANCELED)) {
            throw new FieldConflictException("Cannot publish event because it's already CANCELED");
        }
        if (newStateAction.equals(StateAction.PUBLISH_EVENT) && !actualState.equals(EventState.PENDING)) {
            throw new ValidateException("Cannot publish event because it's not in the right state. Need: PENDING");
        }
        if (newStateAction.equals(StateAction.REJECT_EVENT) && actualState.equals(EventState.PUBLISHED)) {
            throw new FieldConflictException("Cannot rejected event because it's not in the right state.");
        }
    }

    private void checkConstraintState(Event existedEvent) throws FieldConflictException {
        if (existedEvent.getState() == EventState.PUBLISHED) {
            throw new FieldConflictException("Event state must not be PUBLISHED for update event");
        }
    }

    private Optional<Category> getCategoryForPatch(UpdateEventUserRequest updateEventDto) {
        Long newCategoryId = updateEventDto.getCategory();
        return newCategoryId == null ? Optional.empty() : categoryRepository.findById(newCategoryId);
    }

    private Category getExistedCategory(Long catId) throws NotFoundException {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category id=" + catId + " not found"));
    }

    private User getExistedUser(Long userId) throws NotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id=" + userId + " not found"));
    }

    private Event getExistedEvent(Long eventId) throws NotFoundException {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id=" + eventId + " not found"));
    }

    private Integer getCountViews(Long eventId) {
        log.debug("/get views");
        ResponseEntity<List<ViewStatsDto>> responseViews = statsClient.getViewStats(Constant.unreachableStart,
                Constant.unreachableEnd,
                new String[]{"/events/" + eventId},
                null);
        log.debug("Response views: {}", responseViews.toString());
        if (responseViews.getStatusCode().is2xxSuccessful()) return responseViews.getBody().size();
        return 0;
    }

    private void checkConstraintNotPublished(Event existedEvent) throws NotFoundException {
        EventState actualState = existedEvent.getState();
        if (actualState != EventState.PUBLISHED) {
            throw new NotFoundException("Event with id=" + existedEvent.getId() + " not PUBLISHED");
        }
    }

    private void saveEndpointHit(String appName, HttpServletRequest request) {
        statsClient.saveEndpointHit(appName, request.getRequestURI(), request.getRemoteAddr(), LocalDateTime.now());
    }

    private Comparator<EventShortDto> getSort(EventViewSort sort) {
        if (sort == EventViewSort.EVENT_DATE) return Comparator.comparing(EventShortDto::getEventDate);
        return Comparator.comparing(EventShortDto::getViews);
    }

    private LocalDateTime getStartDate(LocalDateTime rangeStart) {
        if (rangeStart == null) return LocalDateTime.now();
        return rangeStart;
    }

    private Map<Long, Long> getConfirmedRequests(List<Event> events) {
        //key - event id , value = count approved requests
        List<Long> searchedEventsIds = events.stream()
                .map(Event::getId)
                .collect(Collectors.toList());
        //key - EventId, value - countConfirmedRequests
        Map<Long, Long> confirmedRequestsMap =
                requestRepository.getCountEventRequests(searchedEventsIds, ParticipationRequestState.CONFIRMED)
                        .stream()
                        .collect(Collectors.toMap(CountEventRequests::getEventId, CountEventRequests::getRequests));
        return confirmedRequestsMap;
    }

    private Map<Long, Long> getViews(List<Event> events) {
        String[] searchedEventsUris = events.stream()
                .map(event -> {
                    return "/events/" + event.getId();
                }).collect(Collectors.toList()).toArray(new String[0]);
        ResponseEntity<List<ViewStatsDto>> views = statsClient.getViewStats(Constant.unreachableStart,
                Constant.unreachableEnd,
                searchedEventsUris,
                false);
        Map<Long, Long> eventIdViews = new HashMap<>();
        views.getBody()
                .forEach(viewStatsDto -> eventIdViews.put(uriToEventId(viewStatsDto.getUri()), viewStatsDto.getHits()));
        return eventIdViews;
    }

    private Long uriToEventId(String uri) {
        String[] uriParts = uri.split("/");
        return Long.parseLong(uriParts[uriParts.length - 1]);
    }

    private List<EventShortDto> setRequestsAndViewsShortDto(List<Event> searchedEvents, Map<Long, Long> requests,
                                                                                                Map<Long, Long> views) {
        List<EventShortDto> resultList = new ArrayList<>();
        searchedEvents.stream().forEach(event -> {
            int requestsToSave = 0;
            int viewsToSave = 0;
            if (requests.containsKey(event.getId())) {
                requestsToSave = requests.get(event.getId()).intValue();
            }
            if (views.containsKey(event.getId())) {
                viewsToSave = views.get(event.getId()).intValue();
            }
            resultList.add(EventMapper.toShortDto(event, requestsToSave, viewsToSave));
        });
        return resultList;
    }

    private List<EventFullDto> setRequestsViewsCommentsToFullDto(List<Event> searchedEvents, Map<Long, Long> requests,
                                                                 Map<Long, Long> views,
                                                                 Map<Long, List<CommentDto>> comments) {
            List<EventFullDto> resultList = new ArrayList<>();
            searchedEvents.stream().forEach(event -> {
                int requestsToSave = 0;
                int viewsToSave = 0;
                if (requests.containsKey(event.getId())) {
                    requestsToSave = requests.get(event.getId()).intValue();
                }
                if (views.containsKey(event.getId())) {
                    viewsToSave = views.get(event.getId()).intValue();
                }
                resultList.add(EventMapper.toFullDto(event, requestsToSave, viewsToSave, comments.get(event.getId())));
            });
            return resultList;
        }

    private List<CommentDto> getComments(Long eventId) {
        List<Comment> comments = commentRepository.findAllByEvent_Id(eventId);
        if (comments == null) return Collections.emptyList();
        return comments.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }
}