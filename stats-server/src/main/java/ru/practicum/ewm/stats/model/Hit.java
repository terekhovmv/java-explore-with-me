package ru.practicum.ewm.stats.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "hits")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class Hit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "app_id")
    App app;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uri_id")
    Uri uri;

    @Column(name = "ip")
    String ip;

    @CreatedDate
    @Column(name = "created")
    LocalDateTime created;
}
