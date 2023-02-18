package ru.practicum.stats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "uris")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Uri {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "path")
    String path;
}
