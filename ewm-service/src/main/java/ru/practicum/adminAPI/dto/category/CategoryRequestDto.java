package ru.practicum.adminAPI.dto.category;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CategoryRequestDto {
    @NotBlank
    private String name;
}