package ru.practicum.controller.privateApi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentDto;
import ru.practicum.comment.service.CommentService;

import javax.validation.Valid;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/users")
public class PrivateCommentController {
    private final CommentService commentService;

    @PostMapping("/{userId}/comments")
    @ResponseStatus(HttpStatus.CREATED)
    public CommentDto createComment(@PathVariable Long userId,
                                    @RequestParam Long eventId,
                                    @Valid @RequestBody NewCommentDto commentDto) {
        log.debug("/create comment");
        return commentService.createComment(userId, eventId, commentDto);
    }

    @GetMapping("/{userId}/comments/{commentId}")
    public CommentDto getCommentById(@PathVariable Long userId,
                                     @PathVariable Long commentId) {
        log.debug("/get comment by id by user");
        return commentService.getCommentByIdFromUser(userId, commentId);
    }

    @GetMapping("/{userId}/comments")
    public List<CommentDto> getCommentsByUser(@PathVariable Long userId,
                                              @RequestParam(defaultValue = "0") Integer from,
                                              @RequestParam(defaultValue = "10") Integer size) {
        log.debug("/get comments by user");
        return commentService.getCommentsByUser(userId, from, size);
    }

    @GetMapping("/{userId}/events/{eventId}/comments")
    public List<CommentDto> getCommentsByEvent(@PathVariable Long userId,
                                               @PathVariable Long eventId) {
        log.debug("/get comments by event");
        return commentService.getCommentsByEventFromUser(userId, eventId);
    }

    @PatchMapping("/{userId}/comments/{commentId}")
    public CommentDto updateComment(@PathVariable Long userId,
                                    @PathVariable Long commentId,
                                    @Valid @RequestBody UpdateCommentDto updateDto) {
        log.debug("/update comment");
        return commentService.updateComment(userId, commentId, updateDto);
    }

    @DeleteMapping("/{userId}/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable Long userId,
                              @PathVariable Long commentId) {
        log.debug("/delete comment");
        commentService.deleteCommentFromUser(userId, commentId);
    }
}