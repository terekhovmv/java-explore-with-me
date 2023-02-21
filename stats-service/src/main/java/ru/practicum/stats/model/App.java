package ru.practicum.stats.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "apps")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class App {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Short id;

    @Column(name = "name")
    String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        App app = (App) o;
        return id != null && Objects.equals(id, app.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
