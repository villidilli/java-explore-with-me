package ru.practicum.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.practicum.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    Page<Comment> findAllByCommentator_Id(Long userId, PageRequest pageRequest);

    List<Comment> findAllByEvent_Id(Long eventId);
}