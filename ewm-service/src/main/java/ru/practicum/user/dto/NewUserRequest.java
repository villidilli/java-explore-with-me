package ru.practicum.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import org.hibernate.validator.constraints.Length;
import ru.practicum.validation.EmailPartsMaxLength;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class NewUserRequest {
    @NotBlank
    @EmailPartsMaxLength
    @Length(min = 6, max = 254)
    private String email;
    @NotBlank
    @Length(min = 2, max = 250)
    private String name;
}