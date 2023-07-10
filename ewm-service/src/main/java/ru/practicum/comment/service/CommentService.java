package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long userId, Long eventId, CommentDto commentDto);

    CommentDto getCommentByIdFromUser(Long userId, Long commentId);

    List<CommentDto> getCommentsByUser(Long userId, Integer from, Integer size);

    List<CommentDto> getCommentsByEventFromUser(Long userId, Long eventId);

    List<CommentDto> getCommentsByEventFromAdmin(Long eventId);

    CommentDto updateComment(Long userId, Long commentId, CommentDto updateDto);

    void deleteCommentFromUser(Long userId, Long commentId);

    void deleteCommentFromAdmin(Long commentId);

    List<CommentDto> getAllComments(Integer from, Integer size);

    CommentDto getCommentByIdFromAdmin(Long commentId);
}