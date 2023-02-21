package ru.practicum.stats.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "uris")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Uri {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @Column(name = "path")
    String path;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Uri uri = (Uri) o;
        return id != null && Objects.equals(id, uri.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
