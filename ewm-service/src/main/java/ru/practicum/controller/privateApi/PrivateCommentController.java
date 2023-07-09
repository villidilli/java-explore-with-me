package ru.practicum.controller.privateApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.service.CommentService;
import ru.practicum.validation.ValidateMarker;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
@Validated
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping("/comments")
    @Validated({ValidateMarker.OnCreate.class})
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long userId,
                                    @RequestParam Long eventId,
                                    @Valid @RequestBody CommentDto commentDto) {
        log.debug("/create comment");
        return commentService.createComment(userId, eventId, commentDto);
    }

    @GetMapping("/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable Long userId,
                                     @PathVariable Long commentId) {
        log.debug("/get comment by id by user");
        return commentService.getCommentByIdFromUser(userId, commentId);
    }

    @GetMapping("/comments")
    public List<CommentDto> getCommentsByUser(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                              @RequestParam(defaultValue = "10") @Positive Integer size) {
        log.debug("/get comments by user");
        return commentService.getCommentsByUser(userId, from, size);
    }

    @GetMapping("/events/{eventId}/comments")
    public List<CommentDto> getCommentsByEvent(@PathVariable Long userId,
                                               @PathVariable Long eventId) {
        log.debug("/get comments by event");
        return commentService.getCommentsByEventFromUser(userId, eventId);
    }

    @PatchMapping("/comments/{commentId}")
    @Validated({ValidateMarker.OnUpdate.class})
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @Valid @RequestBody CommentDto updateDto) {
        log.debug("/update comment");
        return commentService.updateComment(userId, commentId, updateDto);
    }

    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        log.debug("/delete comment");
        commentService.deleteCommentFromUser(userId, commentId);
    }
}