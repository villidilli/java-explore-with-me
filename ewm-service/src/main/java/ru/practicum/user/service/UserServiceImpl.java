package ru.practicum.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.user.dto.UserRequestDto;
import ru.practicum.user.dto.UserResponseDto;
import ru.practicum.user.model.User;
import ru.practicum.user.repository.UserRepository;
import ru.practicum.exception.FieldConflictException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidateException;
import ru.practicum.utils.PageConfig;
import ru.practicum.user.model.UserMapper;

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
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        log.debug("/create user");
        validateEmailParts(userRequestDto.getEmail());
        if (userRepository.findByNameContainsIgnoreCase(userRequestDto.getName()).size() != 0)
            throw new FieldConflictException("Name is already exists");
        User savedUser = userRepository.save(UserMapper.toModel(userRequestDto));
        log.debug("Присвоен id: {}", savedUser.getId());
        return UserMapper.toDto(savedUser);
    }

    @Override
    public List<UserResponseDto> getAllUsers(Long[] ids, Integer from, Integer size) {
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

    private void validateEmailParts(String email) throws ValidateException {
        log.debug("/validate email");
        String[] emailParts = email.split("@");
        String local = emailParts[0];
        String[] domainParts = emailParts[1].split("\\.");
        log.debug("local= {}, domain= {}, total= {}", emailParts[0].length(), emailParts[1].length(), email.length());
        if (local.length() > 64) throw new ValidateException("Email (local) length must be 64 chars");
        for (String domainPart : domainParts) {
            if (domainPart.length() > 63) throw new ValidateException("Email (domain) length must be 63 chars");
        }
    }
}