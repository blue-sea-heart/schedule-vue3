package com.blueseaheart.demo.repository;

import com.blueseaheart.demo.domain.ViewPreference;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ViewPreference entity.
 */
@Repository
public interface ViewPreferenceRepository extends JpaRepository<ViewPreference, Long>, JpaSpecificationExecutor<ViewPreference> {
    default Optional<ViewPreference> findOneWithEagerRelationships(Long id) {
        return this.findOneWithToOneRelationships(id);
    }

    default List<ViewPreference> findAllWithEagerRelationships() {
        return this.findAllWithToOneRelationships();
    }

    default Page<ViewPreference> findAllWithEagerRelationships(Pageable pageable) {
        return this.findAllWithToOneRelationships(pageable);
    }

    @Query(
        value = "select viewPreference from ViewPreference viewPreference left join fetch viewPreference.user",
        countQuery = "select count(viewPreference) from ViewPreference viewPreference"
    )
    Page<ViewPreference> findAllWithToOneRelationships(Pageable pageable);

    @Query("select viewPreference from ViewPreference viewPreference left join fetch viewPreference.user")
    List<ViewPreference> findAllWithToOneRelationships();

    @Query("select viewPreference from ViewPreference viewPreference left join fetch viewPreference.user where viewPreference.id =:id")
    Optional<ViewPreference> findOneWithToOneRelationships(@Param("id") Long id);
}
