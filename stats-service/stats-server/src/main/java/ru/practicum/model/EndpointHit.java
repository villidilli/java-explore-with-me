package ru.practicum.model;

import lombok.Getter;
import lombok.Setter;

import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.constant.StatsConstant;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "hits")
public class EndpointHit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String app;
    private String uri;
    private String ip;
    @DateTimeFormat(pattern = StatsConstant.datetimePattern)
    private LocalDateTime timestamp;
}