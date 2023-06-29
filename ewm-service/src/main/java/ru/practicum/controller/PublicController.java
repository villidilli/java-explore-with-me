package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.EventShortDto;
import ru.practicum.event.service.EventService;
import ru.practicum.utils.Constant;
import ru.practicum.utils.EventViewSort;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class PublicController {
    private final CategoryService categoryService;
    private final EventService eventService;

    @GetMapping("/categories")
    public List<CategoryDto> getAllCategories(@RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        log.debug("/get all categories");
        log.debug("Income parameters: from: {}, size: {}", from, size);
        return categoryService.getAllCategories(from, size);
    }

    @GetMapping("/categories/{catId}")
    public CategoryDto getCategoryById(@PathVariable Long catId) {
        log.debug("/get category by id");
        log.debug("Income parameters: catId: {}", catId);
        return categoryService.getCategoryById(catId);
    }

    //TODO WIP
    @GetMapping("/events")
    public List<EventShortDto> getAllEvents(
                            @RequestParam String text,
                            @RequestParam List<Category> categories,
                            @RequestParam Boolean paid,
                            @RequestParam(required = false)
                                    @DateTimeFormat(pattern = Constant.dateTimeFormat) LocalDateTime rangeStart,
                            @RequestParam(defaultValue = Constant.unreachableEnd)
                                    @DateTimeFormat(pattern = Constant.dateTimeFormat) LocalDateTime rangeEnd,
                            @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                            @RequestParam EventViewSort sort,
                            @RequestParam(defaultValue = "0") Integer from,
                            @RequestParam(defaultValue = "10") Integer size,
                            HttpServletRequest request) {
        log.debug("/get all events");
        return
                eventService.getEventsForPublic(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, size, request);
    }

    @GetMapping("/events/{id}")
    public EventFullDto getEventById(@PathVariable("id") Long eventId,
                                     HttpServletRequest request) {
        log.debug("/get event by id");
        log.debug("Income eventId: {}", eventId);
        return eventService.getEventById(eventId, request);
    }

}