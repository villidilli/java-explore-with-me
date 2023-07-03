package ru.practicum.compilation.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewCompilationDto {
    private List<Long> events;
    @Length(min = 1, max = 50)
    private String title;
    private Boolean pinned = false;
}