package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import ru.practicum.model.ModelToDtoMapper;
import ru.practicum.service.StatService;

import ru.practicum.HitRequestDto;
import ru.practicum.ViewStatsDto;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.constant.Constant.*;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatController {
    private final StatService service;

    @PostMapping("/hit")
    @ResponseStatus(HttpStatus.CREATED)
    public void saveEndpointHit(@Valid @RequestBody HitRequestDto requestDto) {
        log.debug("/saveEndpointHit");
        log.debug("Income DTO: {}", requestDto);
        service.saveHit(ModelToDtoMapper.toHitModel(requestDto));
    }

    @GetMapping("/stats")
    public List<ViewStatsDto> getViewStats(
            @RequestParam(value = "start") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam(value = "end") @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(value = "uris", required = false) String[] uris,
            @RequestParam(value = "unique", defaultValue = "false") Boolean unique) {

        log.debug("/getViewStats");
        log.debug("Period to search {} - {}", start, end);
        log.debug("URIs to search {}", uris);
        log.debug("Unique {}", unique);

        return service.getViewStats(start, end, uris, unique);
    }
}