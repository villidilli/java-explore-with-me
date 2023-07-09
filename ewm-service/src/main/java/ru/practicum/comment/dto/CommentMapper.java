package ru.practicum.comment.dto;

import lombok.experimental.UtilityClass;

import ru.practicum.comment.model.Comment;
import ru.practicum.event.model.Event;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

@UtilityClass
public class CommentMapper {

    public Comment toModel(CommentDto dto, User commentator, Event event) {
        Comment model = new Comment();
        model.setText(dto.getText());
        model.setCommentator(commentator);
        model.setEvent(event);
        model.setTimestamp(LocalDateTime.now());
        return model;
    }

    public Comment patchModel(CommentDto dto, Comment existedComment) {
        existedComment.setText(dto.getText());
        existedComment.setTimestamp(LocalDateTime.now());
        return existedComment;
    }

    public CommentDto toDto(Comment model) {
        CommentDto dto = new CommentDto();
        dto.setId(model.getId());
        dto.setText(model.getText());
        dto.setCommentatorId(model.getCommentator().getId());
        dto.setEventId(model.getEvent().getId());
        dto.setTimestamp(model.getTimestamp());
        return dto;
    }
}