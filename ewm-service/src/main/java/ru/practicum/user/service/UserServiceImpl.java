package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exception.FieldConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.user.dto.NewUserRequest;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.model.UserMapper;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.utils.PageConfig;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    @Transactional
    public UserDto createUser(NewUserRequest userRequestDto) {
        log.debug("/create user");
        if (userRepository.findByNameContainsIgnoreCase(userRequestDto.getName()).size() != 0)
            throw new FieldConflictException("Field: name. Error: name is already exists");
        User savedUser = userRepository.save(UserMapper.toModel(userRequestDto));
        log.debug("Присвоен id: {}", savedUser.getId());
        return UserMapper.toDto(savedUser);
    }

    @Override
    public List<UserDto> getAllUsers(Long[] ids, Integer from, Integer size) {
        log.debug("/get all users");
        if (ids == null) {
            return userRepository.findAll(new PageConfig(from, size, Sort.unsorted())).stream()
                    .map(UserMapper::toDto)
                    .collect(Collectors.toList());
        }
        return userRepository.findAllByIdIn(ids, new PageConfig(from, size, Sort.unsorted())).stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) throws NotFoundException {
        log.debug("/delete user");
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User id: " + userId + " not found"));
        userRepository.deleteById(userId);
    }
}