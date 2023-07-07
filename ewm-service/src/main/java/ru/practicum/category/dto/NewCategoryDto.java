package ru.practicum.category.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewCategoryDto {
    @NotBlank
    @Length(min = 1, max = 50)
    private String name;
}