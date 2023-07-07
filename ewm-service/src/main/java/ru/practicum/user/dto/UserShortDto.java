package ru.practicum.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserShortDto {
    private Long id;
    private String name;
}