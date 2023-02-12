package ru.practicum.ewm.stats.model;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "uris")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Uri {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "path")
    String path;
}
