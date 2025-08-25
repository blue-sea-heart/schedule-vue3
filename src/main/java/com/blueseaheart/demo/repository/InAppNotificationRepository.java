package com.blueseaheart.demo.repository;

import com.blueseaheart.demo.domain.InAppNotification;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the InAppNotification entity.
 */
@Repository
public interface InAppNotificationRepository extends JpaRepository<InAppNotification, Long>, JpaSpecificationExecutor<InAppNotification> {
    @Query("select inAppNotification from InAppNotification inAppNotification where inAppNotification.user.login = ?#{authentication.name}")
    List<InAppNotification> findByUserIsCurrentUser();

    default Optional<InAppNotification> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<InAppNotification> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<InAppNotification> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select inAppNotification from InAppNotification inAppNotification left join fetch inAppNotification.user",
        countQuery = "select count(inAppNotification) from InAppNotification inAppNotification"
    )
    Page<InAppNotification> findAllWithToOneRelationships(Pageable pageable);

    @Query("select inAppNotification from InAppNotification inAppNotification left join fetch inAppNotification.user")
    List<InAppNotification> findAllWithToOneRelationships();

    @Query(
        "select inAppNotification from InAppNotification inAppNotification left join fetch inAppNotification.user where inAppNotification.id =:id"
    )
    Optional<InAppNotification> findOneWithToOneRelationships(@Param("id") Long id);
}
