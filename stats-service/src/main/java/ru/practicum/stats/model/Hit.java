package ru.practicum.stats.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "hits")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    @ToString.Exclude
    App app;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uri_id")
    @ToString.Exclude
    Uri uri;

    @Column(name = "ip")
    String ip;

    @Column(name = "timestamp")
    LocalDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Hit hit = (Hit) o;
        return id != null && Objects.equals(id, hit.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
