package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.category.dto.CategoryRequestDto;
import ru.practicum.category.dto.CategoryResponseDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;
import ru.practicum.exception.FieldConflictException;
import ru.practicum.exception.NotFoundException;


@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;


    @Override
    @Transactional
    public CategoryResponseDto createCategory(CategoryRequestDto categoryRequestDto) {
        log.debug("/create category");
        Category savedCategory = categoryRepository.save(CategoryMapper.toModel(categoryRequestDto));
        log.debug("Присвоен id: {}", savedCategory.getId());
        return CategoryMapper.toDto(savedCategory);
    }

    @Transactional
    @Override
    public void deleteCategory(Long catId) throws NotFoundException {
        log.debug("/delete category");
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category id: " + catId + " not found"));

        //TODO ДОБАВИТЬ ПРОВЕРКУ НА НАЛИЧИЕ У КАТЕГОРИЙ СОБЫТИЙ, ВЕРНУТЬ 409 CONFLICT

        categoryRepository.deleteById(catId);
    }

    @Override
    @Transactional
    public CategoryResponseDto updateCategory(Long catId,
                                              CategoryRequestDto categoryRequestDto) throws NotFoundException {
        log.debug("/update category");
        categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category id: " + catId + " не найден"));

        if(categoryRepository.findByNameContainsIgnoreCase(categoryRequestDto.getName()).size() != 0) {
            throw new FieldConflictException("Name is already exists");
        }

        Category updatedCategory = categoryRepository.save(CategoryMapper.toModel(categoryRequestDto));
        return CategoryMapper.toDto(updatedCategory);
    }
}