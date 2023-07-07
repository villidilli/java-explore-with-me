package ru.practicum.comment.service;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.NewCommentDto;
import ru.practicum.comment.dto.UpdateCommentDto;

import java.util.List;

public interface CommentService {
    CommentDto createComment(Long userId, Long eventId, NewCommentDto commentDto);

    CommentDto getCommentByIdFromUser(Long userId, Long commentId);

    List<CommentDto> getCommentsByUser(Long userId, Integer from, Integer size);

    List<CommentDto> getCommentsByEventFromUser(Long userId, Long eventId, Integer from, Integer size);

    CommentDto updateComment(Long userId, Long commentId, UpdateCommentDto updateDto);

    void deleteCommentFromUser(Long userId, Long commentId);

    void deleteCommentFromAdmin(Long commentId);

    List<CommentDto> getAllComments(Integer from, Integer size);

    CommentDto getCommentByIdFromAdmin(Long commentId);

    List<CommentDto> getCommentsByEventFromAdmin(Long eventId, Integer from, Integer size);
}