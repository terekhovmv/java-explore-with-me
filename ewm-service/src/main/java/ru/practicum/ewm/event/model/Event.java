package ru.practicum.ewm.event.model;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.user.model.User;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id")
    @ToString.Exclude
    User initiator; //*

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    Category category;

    @Column(name = "title")
    String title;

    @Column(name = "annotation")
    String annotation;

    @Column(name = "description")
    String description;

    @Column(name = "event_date")
    LocalDateTime eventDate;

    @Column(name = "location_lat")
    Float locationLat;

    @Column(name = "location_lon")
    Float locationLon;

    @Column(name = "paid")
    Boolean paid;

    @Column(name = "request_moderation")
    Boolean requestModeration;

    @Column(name = "participant_limit")
    Integer participantLimit;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    EventState state;

    @CreatedDate
    @Column(name = "created_on")
    LocalDateTime createdOn;

    @Column(name = "published_on")
    LocalDateTime publishedOn;

    @Column(name = "confirmed_requests")
    Long confirmedRequests;
}
