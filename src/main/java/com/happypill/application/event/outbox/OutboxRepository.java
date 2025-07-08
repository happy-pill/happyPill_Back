package com.happypill.application.event.outbox;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;

public interface OutboxRepository extends JpaRepository<Outbox, Long> {

    @Query("SELECT o FROM Outbox o WHERE o.retryCount > 0 AND o.createdAt > :createdAfter")
    List<Outbox> findRetryableCreatedAfter(@Param("createdAfter") ZonedDateTime createdAfter);

    @Modifying
    @Query(value = "UPDATE outbox SET retry_count = retry_count - 1 WHERE id = :id", nativeQuery = true)
    int decrementRetryCount(@Param("id") Long id);

}
