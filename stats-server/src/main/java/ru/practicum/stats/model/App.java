package ru.practicum.stats.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "apps")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Short id;

    @Column(name = "name")
    String name;
}
