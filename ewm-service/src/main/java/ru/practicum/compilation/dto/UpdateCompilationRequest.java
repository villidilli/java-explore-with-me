package ru.practicum.compilation.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UpdateCompilationRequest {
    @Length(min = 1, max = 50)
    private String title;
    private Boolean pinned;
    private List<Long> events;
}