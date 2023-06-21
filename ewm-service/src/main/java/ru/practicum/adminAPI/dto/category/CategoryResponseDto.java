package ru.practicum.adminAPI.dto.category;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryResponseDto {
    private Long id;
    private String name;
}