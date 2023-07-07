package ru.practicum.comment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.comment.model.Comment;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

//    @Query("SELECT c " +
//            "FROM Comment AS c " +
//            "WHERE c.commentator.id = :userId")
//    Page<Comment> getCommentsByUser(Long userId, PageRequest page);

    Page<Comment> findAllByCommentator_Id(Long userId, PageRequest pageRequest);

//    @Query("SELECT c " +
//            "FROM Comment AS c " +
//            "WHERE c.event.id = :eventId")
//    List<Comment> getCommentsByEvent(Long eventId);

    List<Comment> findAllByEvent_Id(Long eventId);
}