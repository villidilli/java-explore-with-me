package ru.practicum.adminAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.adminAPI.dto.user.UserRequestDto;
import ru.practicum.adminAPI.dto.user.UserResponseDto;
import ru.practicum.adminAPI.model.User;
import ru.practicum.adminAPI.repository.UserRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.utils.PageConfig;
import ru.practicum.utils.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.utils.mapper.UserMapper.toDto;
import static ru.practicum.utils.mapper.UserMapper.toModel;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        log.debug("/create user");
        User savedUser = userRepository.save(toModel(userRequestDto));
        log.debug("Присвоен id: {}", savedUser.getId());
        return toDto(savedUser);
    }

    @Override
    public List<UserResponseDto> getAllUsers(Integer[] ids, Integer from, Integer size) {
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
    public void deleteUser(Long userId) {
        log.debug("/delete user");
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User с id: " + userId + " не найден"));
        userRepository.deleteById(userId);
    }
}