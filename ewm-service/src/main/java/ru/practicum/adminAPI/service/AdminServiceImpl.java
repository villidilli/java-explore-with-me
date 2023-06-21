package ru.practicum.adminAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.adminAPI.dto.UserRequestDto;
import ru.practicum.adminAPI.model.User;
import ru.practicum.adminAPI.repository.UserRepository;

import static ru.practicum.utils.UserMapper.toModel;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;

    @Override
    public User createUser(UserRequestDto userRequestDto) {
        log.debug("/create user");
        User savedUser = userRepository.save(toModel(userRequestDto));
        log.debug("Присвоен id: {}", savedUser.getId());
        return savedUser;
    }
}