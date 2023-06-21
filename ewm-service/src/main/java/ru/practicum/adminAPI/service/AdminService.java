package ru.practicum.adminAPI.service;

import ru.practicum.adminAPI.dto.UserRequestDto;
import ru.practicum.adminAPI.model.User;

public interface AdminService {
    User createUser(UserRequestDto userRequestDto);
}
