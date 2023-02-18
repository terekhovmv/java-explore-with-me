package ru.practicum.stats.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "hits")
@Data
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

    @Column(name = "timestamp")
    LocalDateTime timestamp;
}
