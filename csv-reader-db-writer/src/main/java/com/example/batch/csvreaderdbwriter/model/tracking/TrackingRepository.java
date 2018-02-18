package com.example.batch.csvreaderdbwriter.model.tracking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface TrackingRepository extends JpaRepository<Tracking, Long> {

    @Transactional
    @Modifying
    @Query(value = "update tracking set end_time = now(6) where id = ?1", nativeQuery = true)
    void updateEndTime(Long trackingId);

}
