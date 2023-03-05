package ru.practicum.ewm.subscription.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.ewm.exception.NotFoundException;
import ru.practicum.ewm.subscription.model.Subscription;
import ru.practicum.ewm.subscription.model.SubscriptionInfo;
import ru.practicum.ewm.user.model.User;

import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    default Subscription require(long subscriberId, long promoterId) {
        return findFirstBySubscriberIdAndPromoterId(subscriberId, promoterId)
                .orElseThrow(
                        () -> new NotFoundException(
                                String.format("Subscription of user #%d to user #%d was not found", subscriberId, promoterId)
                        )
                );
    }

    Optional<Subscription> findFirstBySubscriberIdAndPromoterId(long subscriberId, long promoterId);

    @Query(
            "SELECT s.promoter FROM Subscription s " +
                    "WHERE s.subscriber.id = :subscriberId " +
                    "ORDER BY s.promoter.name ASC"
    )
    Page<User> getSubscribed(@Param("subscriberId") long subscriberId, Pageable pageable);

    @Query(
            "SELECT s.promoter.id FROM Subscription s " +
                    "WHERE s.subscriber.id = :subscriberId"
    )
    List<Long> getSubscribedIds(@Param("subscriberId") long subscriberId);

    @Query("SELECT COUNT(s) FROM Subscription s WHERE s.promoter.id = :promoterId")
    long countSubscribers(@Param("promoterId") long promoterId);

    @Query(
            "SELECT NEW ru.practicum.ewm.subscription.model.SubscriptionInfo(s.promoter, COUNT(s.id)) " +
                    "FROM Subscription s GROUP BY s.promoter.id ORDER BY COUNT(s.id) DESC"
    )
    Page<SubscriptionInfo> getTopInfos(Pageable pageable);
}
