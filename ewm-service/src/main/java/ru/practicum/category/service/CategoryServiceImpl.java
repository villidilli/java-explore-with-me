package ru.practicum.category.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ru.practicum.category.dto.NewCategoryDto;
import ru.practicum.category.dto.CategoryDto;
import ru.practicum.category.model.Category;
import ru.practicum.category.model.CategoryMapper;
import ru.practicum.category.repository.CategoryRepository;

import ru.practicum.event.model.Event;
import ru.practicum.event.repository.EventRepository;

import ru.practicum.exception.FieldConflictException;
import ru.practicum.exception.NotFoundException;

import ru.practicum.utils.PageConfig;

import java.util.List;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    public CategoryDto createCategory(NewCategoryDto categoryRequestDto) {
        log.debug("/create category");
        Category savedCategory = categoryRepository.save(CategoryMapper.toModel(categoryRequestDto));
        log.debug("Присвоен id: {}", savedCategory.getId());
        return CategoryMapper.toDto(savedCategory);
    }

    @Override
    @Transactional
    public void deleteCategory(Long catId) {
        log.debug("/delete category");
        getExistedCategory(catId);
        checkConstraintUsingEvents(catId);
        categoryRepository.deleteById(catId);
    }

    @Override
    public List<CategoryDto> getAllCategories(Integer from, Integer size) {
        log.debug("/get all categories");
        return categoryRepository.findAll(new PageConfig(from, size, Sort.unsorted())).stream()
                .map(CategoryMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryDto getCategoryById(Long catId) {
        log.debug("/get category by id");
        return CategoryMapper.toDto(getExistedCategory(catId));
    }

    @Override
    @Transactional
    public CategoryDto updateCategory(Long catId, NewCategoryDto categoryRequestDto) throws NotFoundException {
        log.debug("/update category");
        Category existedCategory = getExistedCategory(catId);
        if (existedCategory.getName().equals(categoryRequestDto.getName())) {
            return CategoryMapper.toDto(existedCategory);
        }
        checkConstraintNameExisted(categoryRequestDto.getName(), catId);
        Category updatedCategory = categoryRepository.save(CategoryMapper.toModel(categoryRequestDto));
        return CategoryMapper.toDto(updatedCategory);
    }

    private void checkConstraintUsingEvents(Long catId) throws FieldConflictException {
        List<Event> usingEvents = eventRepository.findAllByCategory_Id(catId);
        if (usingEvents.size() != 0) throw new FieldConflictException("Many events using deleting category");
    }

    private Category getExistedCategory(Long catId) throws NotFoundException {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Category with id=" + catId + " was not found"));
    }

    private void checkConstraintNameExisted(String newName, Long catId) throws FieldConflictException {
        if (categoryRepository.findByNameContainsIgnoreCaseAndIdIsNot(newName, catId).size() != 0) {
            throw new FieldConflictException("Field: name. Error: name is already exists");
        }
    }
}