package com.blueseaheart.demo.repository;

import com.blueseaheart.demo.domain.Schedule;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Schedule entity.
 *
 * When extending this class, extend ScheduleRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface ScheduleRepository
    extends ScheduleRepositoryWithBagRelationships, JpaRepository<Schedule, Long>, JpaSpecificationExecutor<Schedule> {
    @Query("select schedule from Schedule schedule where schedule.owner.login = ?#{authentication.name}")
    List<Schedule> findByOwnerIsCurrentUser();

    default Optional<Schedule> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findOneWithToOneRelationships(id));
    }

    default List<Schedule> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships());
    }

    default Page<Schedule> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAllWithToOneRelationships(pageable));
    }

    @Query(
        value = "select schedule from Schedule schedule left join fetch schedule.owner left join fetch schedule.status left join fetch schedule.category",
        countQuery = "select count(schedule) from Schedule schedule"
    )
    Page<Schedule> findAllWithToOneRelationships(Pageable pageable);

    @Query(
        "select schedule from Schedule schedule left join fetch schedule.owner left join fetch schedule.status left join fetch schedule.category"
    )
    List<Schedule> findAllWithToOneRelationships();

    @Query(
        "select schedule from Schedule schedule left join fetch schedule.owner left join fetch schedule.status left join fetch schedule.category where schedule.id =:id"
    )
    Optional<Schedule> findOneWithToOneRelationships(@Param("id") Long id);
}
