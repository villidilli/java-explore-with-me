package ru.practicum.comment.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.comment.dto.CommentDto;
import ru.practicum.comment.dto.CommentMapper;
import ru.practicum.comment.model.Comment;
import ru.practicum.comment.repository.CommentRepository;

import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;

import ru.practicum.exception.FieldConflictException;
import ru.practicum.exception.NotFoundException;

import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;

import ru.practicum.utils.PageConfig;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CommentDto createComment(Long userId, Long eventId, CommentDto commentDto) {
        log.debug("/create comment");
        User commentator = getExistedUser(userId);
        Event event = getExistedEvent(eventId);
        checkConstraintCommentatorNotEventInitiator(commentator, event);
        Comment savedComment = commentRepository.save(CommentMapper.toModel(commentDto, commentator, event));
        log.debug("Saved comment id: {}", savedComment.getId());
        return CommentMapper.toDto(savedComment);
    }

    @Override
    public CommentDto getCommentByIdFromUser(Long userId, Long commentId) {
        //пользователь может получать все комментарии, в т.ч. не свои
        log.debug("/get comment by id from user");
        getExistedUser(userId);
        Comment existedComment = getExistedComment(commentId);
        return CommentMapper.toDto(existedComment);
    }

    @Override
    public List<CommentDto> getCommentsByUser(Long userId, Integer from, Integer size) {
        log.debug("/get comments by user");
        getExistedUser(userId);
        PageRequest pageRequest = new PageConfig(from, size, Sort.unsorted());
        Page<Comment> searchedComment = commentRepository.findAllByCommentator_Id(userId, pageRequest);
        return searchedComment.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CommentDto> getCommentsByEventFromUser(Long userId, Long eventId) {
        log.debug("/get comments by event from user");
        //получение всех комментариев к событию, в т.ч. к чужому без ограничений
        getExistedUser(userId);
        getExistedEvent(eventId);
        List<Comment> searchedComments = commentRepository.findAllByEvent_Id(eventId);
        return searchedComments.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }


    @Override
    public List<CommentDto> getCommentsByEventFromAdmin(Long eventId) {
        log.debug("/get comments by event from admin");
        getExistedEvent(eventId);
        List<Comment> searchedComments = commentRepository.findAllByEvent_Id(eventId);
        return searchedComments.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto updateComment(Long userId, Long commentId, CommentDto updateDto) {
        log.debug("/update comment");
        User requester = getExistedUser(userId);
        Comment existedComment = getExistedComment(commentId);
        checkConstraintRequesterIsCommentOwner(requester, existedComment);
        Comment updatedComment = CommentMapper.patchModel(updateDto, existedComment);
        Comment savedComment = commentRepository.save(updatedComment);
        return CommentMapper.toDto(savedComment);
    }

    @Override
    @Transactional
    public void deleteCommentFromUser(Long userId, Long commentId) {
        log.debug("/delete comment");
        User requester = getExistedUser(userId);
        Comment existedComment = getExistedComment(commentId);
        checkConstraintRequesterIsCommentOwner(requester, existedComment);
        commentRepository.deleteById(commentId);
    }

    @Override
    @Transactional
    public void deleteCommentFromAdmin(Long commentId) {
        log.debug("/delete comment from admin");
        getExistedComment(commentId);
        commentRepository.deleteById(commentId);
    }

    @Override
    public List<CommentDto> getAllComments(Integer from, Integer size) {
        log.debug("/get all comments");
        Page<Comment> searchedComments = commentRepository.findAll(new PageConfig(from, size, Sort.unsorted()));
        return searchedComments.stream()
                .map(CommentMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CommentDto getCommentByIdFromAdmin(Long commentId) {
        log.debug("/get comment by id from admin");
        Comment existedComment = getExistedComment(commentId);
        return CommentMapper.toDto(existedComment);
    }

    private void checkConstraintRequesterIsCommentOwner(User requester, Comment existedComment)
                                                                                        throws FieldConflictException {
        if (!Objects.equals(existedComment.getCommentator().getId(), requester.getId())) {
            throw new FieldConflictException("User with id: " + requester.getId() + " is not the owner of the comment");
        }
    }

    private void checkConstraintCommentatorNotEventInitiator(User commentator, Event event)
                                                                                        throws FieldConflictException {
        if (Objects.equals(commentator.getId(), event.getInitiator().getId())) {
            throw new FieldConflictException("Initiator can't comment his event");
        }
    }

    private Comment getExistedComment(Long commentId) throws NotFoundException {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException("Comment with id: " + commentId + " not found"));
    }

    private User getExistedUser(Long userId) throws NotFoundException {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("User with id: " + userId + " not found"));
    }

    private Event getExistedEvent(Long eventId) throws NotFoundException {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException("Event with id: " + eventId + " not found"));
    }
}