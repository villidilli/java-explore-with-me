package ru.practicum.comment.dto;

import lombok.*;
import org.hibernate.validator.constraints.Length;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class NewCommentDto {
    @Length(min = 1, max = 7000)
    @NotBlank
    private String text;
}