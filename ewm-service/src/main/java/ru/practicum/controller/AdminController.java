package ru.practicum.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.service.CategoryService;
import ru.practicum.event.dto.EventFullDto;
import ru.practicum.event.dto.UpdateEventUserRequest;
import ru.practicum.event.model.EventState;
import ru.practicum.event.service.EventService;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.service.UserService;
import ru.practicum.utils.Constant;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final UserService adminService;
    private final CategoryService categoryService;
    private final EventService eventService;

    // USERS --->

    @GetMapping("/users")
    public List<UserDto> getAllUsers(@RequestParam(required = false) Long[] ids,
                                     @RequestParam(defaultValue = "0") Integer from,
                                     @RequestParam(defaultValue = "10") Integer size) {
        log.debug("/Income parameters: userIds = {}, from = {}, size = {}", ids, from, size);
        return adminService.getAllUsers(ids, from, size);
    }

    @PostMapping("/users")
    @ResponseStatus(HttpStatus.CREATED)
    public UserDto createUser(@Valid @RequestBody NewUserRequest userRequestDto) {
        log.debug("UserDto to create: {}", userRequestDto.toString());
        return adminService.createUser(userRequestDto);
    }

    @DeleteMapping("/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long userId) {
        log.debug("/delete user");
        log.debug("Запрошено удаление user с id: {}", userId);
        adminService.deleteUser(userId);
    }

    // CATEGORIES --->

    @PostMapping("/categories")
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDto createCategory(@Valid @RequestBody NewCategoryDto categoryRequestDto) {
        log.debug("CategoryDto to create: {}", categoryRequestDto.toString());
        return categoryService.createCategory(categoryRequestDto);
    }

    @DeleteMapping("/categories/{catId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCategory(@PathVariable Long catId) {
        log.debug("/delete category");
        log.debug("Income parameters: catId: {}", catId);
        categoryService.deleteCategory(catId);
    }

    @PatchMapping("/categories/{catId}")
    public CategoryDto updateCategory(@PathVariable Long catId,
                                      @Valid @RequestBody NewCategoryDto categoryRequestDto) {
        log.debug("/update category");
        log.debug("Income parameters: catId: {}, body: {}", catId, categoryRequestDto.toString());
        return categoryService.updateCategory(catId, categoryRequestDto);
    }

    // EVENTS --->

    @GetMapping("/events")
    public List<EventFullDto> getEventsForAdmin(
                                    @RequestParam(required = false) List<Long> users,
                                    @RequestParam(required = false) List<EventState> states,
                                    @RequestParam(required = false) List<Long> categories,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = Constant.dateTimeFormat)
                                        LocalDateTime rangeStart,
                                    @RequestParam(required = false) @DateTimeFormat(pattern = Constant.dateTimeFormat)
                                        LocalDateTime rangeEnd,
                                    @RequestParam(defaultValue = "0") Integer from,
                                    @RequestParam(defaultValue = "10") Integer size) {
        log.debug("/get events");
        log.debug(
        "Income parameters: users: {}, states: {}, categories: {}, rangeStart: {}, rangeEnd: {}, from: {}, size: {}",
        users, states, categories, rangeStart, rangeEnd, from, size);
        return eventService.getEventsForAdmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }

    @PatchMapping("/events/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @RequestBody UpdateEventUserRequest eventDto,
                                    HttpServletRequest request) {
        log.debug("/updateEvent");
        log.debug("Income parameters: eventId: {}, eventDto: {}", eventId, eventDto.toString());
        log.debug(request.getContextPath());
        log.debug(request.getRequestURI());
        log.debug(request.getRemoteUser());
        log.debug(request.getRemoteAddr());
        return eventService.updateEventAdmin(eventId, eventDto);
    }


}