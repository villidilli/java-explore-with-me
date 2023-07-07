package ru.practicum.event.model;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Location {
    @NotBlank
    private Float lat;
    @NotBlank
    private Float lon;
}