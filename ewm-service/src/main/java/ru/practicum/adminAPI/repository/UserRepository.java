package ru.practicum.adminAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.adminAPI.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

}