package ru.practicum.stats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "apps")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Short id;

    @Column(name = "name")
    String name;
}
