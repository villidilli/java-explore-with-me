package ru.practicum.adminAPI.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.adminAPI.dto.category.CategoryRequestDto;
import ru.practicum.adminAPI.dto.category.CategoryResponseDto;
import ru.practicum.adminAPI.dto.user.UserRequestDto;
import ru.practicum.adminAPI.dto.user.UserResponseDto;
import ru.practicum.adminAPI.model.Category;
import ru.practicum.adminAPI.model.User;
import ru.practicum.adminAPI.repository.CategoryRepository;
import ru.practicum.adminAPI.repository.UserRepository;
import ru.practicum.exception.NotFoundException;
import ru.practicum.exception.ValidateException;
import ru.practicum.utils.PageConfig;
import ru.practicum.utils.mapper.CategoryMapper;
import ru.practicum.utils.mapper.UserMapper;

import java.util.List;
import java.util.stream.Collectors;


@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;


    @Override
    @Transactional
    public UserResponseDto createUser(UserRequestDto userRequestDto) {
        log.debug("/create user");
        User savedUser = userRepository.save(UserMapper.toModel(userRequestDto));
        log.debug("Присвоен id: {}", savedUser.getId());
        return UserMapper.toDto(savedUser);
    }

    @Override
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        log.debug("/create category");
        Category savedCategory = categoryRepository.save(CategoryMapper.toModel(categoryRequestDto));
        log.debug("Присвоен id: {}", savedCategory.getId());
        return CategoryMapper.toDto(savedCategory);
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
    @Transactional
    public void deleteUser(Long userId) {
        log.debug("/delete user");
        userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User id: " + userId + " not found"));
        userRepository.deleteById(userId);
    }

    @Override
    public void deleteCategory(Long catId) {
        log.debug("/delete category");
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category id: " + catId + " not found"));

        //TODO ДОБАВИТЬ ПРОВЕРКУ НА НАЛИЧИЕ У КАТЕГОРИЙ СОБЫТИЙ, ВЕРНУТЬ 409 CONFLICT

        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategory(Long catId, CategoryRequestDto categoryRequestDto) {
        log.debug("/update category");
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category id: " + catId + " не найден"));

        if(categoryRepository.findByNameContainsIgnoreCase(categoryRequestDto.getName()).size() != 0) {
            throw new ValidateException("Name is already exists");
        }

        Category updatedCategory = categoryRepository.save(CategoryMapper.toModel(categoryRequestDto));
        return CategoryMapper.toDto(updatedCategory);
    }
}