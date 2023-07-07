package ru.practicum.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import ru.practicum.comment.model.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c " +
            "FROM Comment AS c " +
            "WHERE c.commentator.id = :userId")
    Page<Comment> getCommentsByUser(Long userId, PageRequest page);

    @Query("SELECT c " +
            "FROM Comment AS c " +
            "WHERE c.event.id = :eventId")
    Page<Comment> getCommentsByEvent(Long eventId, PageRequest pageRequest);
}