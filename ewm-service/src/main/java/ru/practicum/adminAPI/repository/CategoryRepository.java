package ru.practicum.adminAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.adminAPI.model.Category;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByNameContainsIgnoreCase(String name);
}