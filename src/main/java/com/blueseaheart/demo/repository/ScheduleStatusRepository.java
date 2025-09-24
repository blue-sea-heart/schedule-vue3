package com.blueseaheart.demo.repository;

import com.blueseaheart.demo.domain.ScheduleStatus;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ScheduleStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduleStatusRepository extends JpaRepository<ScheduleStatus, Long>, JpaSpecificationExecutor<ScheduleStatus> {
    Optional<ScheduleStatus> findByCode(String code);
    Optional<ScheduleStatus> findFirstByIsDefaultTrue();
}
