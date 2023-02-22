package ru.practicum.ewm.event.mapping;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.ewm.api.dto.EventFullDto;
import ru.practicum.ewm.api.dto.EventShortDto;
import ru.practicum.ewm.api.dto.Location;
import ru.practicum.ewm.api.dto.NewEventDto;
import ru.practicum.ewm.category.mapping.CategoryMapper;
import ru.practicum.ewm.category.model.Category;
import ru.practicum.ewm.event.model.Event;
import ru.practicum.ewm.user.mapping.UserMapper;
import ru.practicum.ewm.user.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final UserMapper userMapper;

    private final CategoryMapper categoryMapper;

    private final EventStateMapper eventStateMapper;

    private static String dateTimeToString(LocalDateTime from) {
        if (from == null) {
            return null;
        }

        return DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(from);
    }

    private static LocalDateTime stringToDateTime(String from) {
        if (from == null) {
            return null;
        }

        return LocalDateTime.parse(
                from,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        );
    }

    public EventFullDto toDto(Event from, long views) {
        return new EventFullDto()
                .id(from.getId())
                .initiator(userMapper.toShortDto(from.getInitiator()))
                .category(categoryMapper.toDto(from.getCategory()))
                .title(from.getTitle())
                .annotation(from.getAnnotation())
                .description(from.getDescription())
                .eventDate(dateTimeToString(from.getEventDate()))
                .location(
                        new Location()
                                .lat(from.getLocationLat())
                                .lon(from.getLocationLon())
                )
                .paid(from.getPaid())
                .requestModeration(from.getRequestModeration())
                .participantLimit(from.getParticipantLimit())
                .state(eventStateMapper.toDtoState(from.getState()))
                .createdOn(dateTimeToString(from.getCreatedOn()))
                .publishedOn(dateTimeToString(from.getPublishedOn()))
                .confirmedRequests(from.getConfirmedRequests())
                .views(views);
    }

    public EventShortDto toShortDto(Event from, long views) {
        return new EventShortDto()
                .id(from.getId())
                .initiator(userMapper.toShortDto(from.getInitiator()))
                .category(categoryMapper.toDto(from.getCategory()))
                .title(from.getTitle())
                .annotation(from.getAnnotation())
                .eventDate(dateTimeToString(from.getEventDate()))
                .paid(from.getPaid())
                .confirmedRequests(from.getConfirmedRequests())
                .views(views);
    }

    public Event transientFromDto(NewEventDto from, User initiator, Category category) {
        return Event.builder()
                .id(null)
                .initiator(initiator)
                .category(category)
                .title(from.getTitle())
                .annotation(from.getAnnotation())
                .description(from.getDescription())
                .eventDate(stringToDateTime(from.getEventDate()))
                .locationLat(from.getLocation().getLat())
                .locationLon(from.getLocation().getLon())
                .paid(from.isPaid())
                .requestModeration(from.isRequestModeration())
                .participantLimit(from.getParticipantLimit())
                .state(eventStateMapper.toState(EventFullDto.StateEnum.PENDING))
                .createdOn(null)
                .publishedOn(null)
                .confirmedRequests(null)
                .build();
    }
}
