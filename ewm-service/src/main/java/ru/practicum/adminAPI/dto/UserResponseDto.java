package ru.practicum.adminAPI.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserResponseDto {
    private Long id;
    private String email;
    private String name;
}