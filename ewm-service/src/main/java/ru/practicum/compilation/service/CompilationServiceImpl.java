package ru.practicum.compilation.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.StatsServiceClient;

import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.dto.CompilationMapper;
import ru.practicum.compilation.dto.NewCompilationDto;
import ru.practicum.compilation.dto.UpdateCompilationRequest;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.compilation.repository.CompilationRepository;

import ru.practicum.dto.ViewStatsDto;

import ru.practicum.event.dto.EventMapper;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;

import ru.practicum.exception.NotFoundException;

import ru.practicum.request.model.CountEventRequests;
import ru.practicum.request.model.ParticipationRequestState;
import ru.practicum.request.repository.ParticipationRequestRepository;

import ru.practicum.utils.Constant;
import ru.practicum.utils.PageConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository compRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository requestRepository;
    private final StatsServiceClient statsClient;

    @Override
    @Transactional
    public CompilationDto createCompilation(NewCompilationDto compilationDto) {
        log.debug("/create compilation");
        List<Event> existedEvents = eventRepository.findAllById(compilationDto.getEvents());
        Compilation savedCompilation = compRepository.save(CompilationMapper.toModel(compilationDto, existedEvents));
        log.debug("Saved id: {}", savedCompilation.getId());
        Map<Long, Long> requests = getConfirmedRequests(existedEvents);
        Map<Long, Long> views = getViews(existedEvents);
        List<EventShortDto> eventShortDtos = setRequestsAndViewsShortDto(existedEvents, requests, views);
        return CompilationMapper.toDto(savedCompilation, eventShortDtos);
    }

    @Override
    @Transactional
    public void deleteCompilation(Long compId) {
        log.debug("/delete compilation");
        getExistedCompilation(compId);
        compRepository.deleteById(compId);
    }

    @Override
    @Transactional
    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationDto) {
        log.debug("/update compilation");
        Compilation existedCompilation = getExistedCompilation(compId);
        List<Event> existedEvents = eventRepository.findAllById(updateCompilationDto.getEvents());
        Compilation updatedCompilation =
                CompilationMapper.patchMappingToModel(existedCompilation, updateCompilationDto, existedEvents);
        Compilation savedCompilation = compRepository.save(updatedCompilation);
        Map<Long, Long> requests = getConfirmedRequests(existedEvents);
        Map<Long, Long> views = getViews(existedEvents);
        List<EventShortDto> eventShortDtos = setRequestsAndViewsShortDto(existedEvents, requests, views);
        return CompilationMapper.toDto(savedCompilation, eventShortDtos);
    }

    @Override
    public List<CompilationDto> getAllCompilations(Boolean pinned, Integer from, Integer size) {
        log.debug("/get all compilations");
        List<Compilation> searchedCompilations =
                compRepository.getAllCompilations(pinned, new PageConfig(from, size, Sort.unsorted()));
        List<Event> compilationEvents = new ArrayList<>();
        searchedCompilations.forEach(compilation -> compilationEvents.addAll(compilation.getEvents()));
        Map<Long, Long> requests = getConfirmedRequests(compilationEvents);
        Map<Long, Long> views = getViews(compilationEvents);
        List<CompilationDto> result = new ArrayList<>();
        searchedCompilations
        .forEach(compilation -> result.add(CompilationMapper.toDto(compilation,
                                                                   setRequestsAndViewsShortDto(compilation.getEvents(),
                                                                                                requests,
                                                                                                views))));
        return result;
    }

    @Override
    public CompilationDto getCompilationById(Long compId) {
        log.debug("/get compilation by id");
        Compilation compilation = getExistedCompilation(compId);
        Map<Long, Long> requests = getConfirmedRequests(compilation.getEvents());
        Map<Long, Long> views = getViews(compilation.getEvents());
        return CompilationMapper.toDto(compilation,
                                        setRequestsAndViewsShortDto(compilation.getEvents(), requests, views));
    }

    private Compilation getExistedCompilation(Long compId) throws NotFoundException {
        return compRepository.findById(compId)
                .orElseThrow(() -> new NotFoundException("Compilation id=" + compId + " not found"));
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
                })
                .collect(Collectors.toList())
                .toArray(new String[0]);
        ResponseEntity<List<ViewStatsDto>> views = statsClient.getViewStats(Constant.unreachableStart,
                                                                            Constant.unreachableEnd,
                                                                            searchedEventsUris,
                                                                     false);
        Map<Long, Long> eventIdViews = new HashMap<>();
        views.getBody().forEach(viewStatsDto -> eventIdViews.put(uriToEventId(viewStatsDto.getUri()),
                                                                 viewStatsDto.getHits()));
        return eventIdViews;
    }

    private List<EventShortDto> setRequestsAndViewsShortDto(List<Event> searchedEvents, Map<Long, Long> requests,
                                                                                        Map<Long, Long> views) {
        List<EventShortDto> resultList = new ArrayList<>();
        searchedEvents.stream()
                .forEach(event -> {
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

    private Long uriToEventId(String uri) {
        String[] uriParts = uri.split("/");
        return Long.parseLong(uriParts[uriParts.length - 1]);
    }
}