package ru.practicum.controller.adminApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.CommentService;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/admin")
@Validated
public class AdminCommentController {
    private final CommentService commentService;

    @GetMapping("/comments")
    public List<CommentDto> getAllComments(@RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                           @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug("/get all comments");
        return commentService.getAllComments(from, size);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable Long commentId) {
        log.debug("/get comment by id");
        return commentService.getCommentByIdFromAdmin(commentId);
    }

    @GetMapping("/users/{userId}/comments")
    public List<CommentDto> getCommentsByUser(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug("/get comments by user");
        return commentService.getCommentsByUser(userId, from, size);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getCommentsByEvent(@PathVariable Long eventId) {
        log.debug("/get comments by event");
        return commentService.getCommentsByEventFromAdmin(eventId);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCommentById(@PathVariable Long commentId) {
        log.debug("/delete comment");
        commentService.deleteCommentFromAdmin(commentId);
    }
}