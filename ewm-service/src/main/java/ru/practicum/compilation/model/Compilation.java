package ru.practicum.compilation.model;

import lombok.*;
import ru.practicum.event.model.Event;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "compilations")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private Boolean pinned;
    @ManyToMany
    @JoinTable(name = "compilations_events",
               joinColumns = @JoinColumn(name = "compilation_id"),
               inverseJoinColumns = @JoinColumn(name = "event_id"))
    private List<Event> events;
}