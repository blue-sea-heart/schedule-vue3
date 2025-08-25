package com.blueseaheart.demo.repository;

import com.blueseaheart.demo.domain.ScheduleStatus;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ScheduleStatus entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduleStatusRepository extends JpaRepository<ScheduleStatus, Long>, JpaSpecificationExecutor<ScheduleStatus> {}
