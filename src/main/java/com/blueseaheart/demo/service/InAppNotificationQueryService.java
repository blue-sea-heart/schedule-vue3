package com.blueseaheart.demo.service;

import com.blueseaheart.demo.domain.*; // for static metamodels
import com.blueseaheart.demo.domain.InAppNotification;
import com.blueseaheart.demo.repository.InAppNotificationRepository;
import com.blueseaheart.demo.service.criteria.InAppNotificationCriteria;
import com.blueseaheart.demo.service.dto.InAppNotificationDTO;
import com.blueseaheart.demo.service.mapper.InAppNotificationMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link InAppNotification} entities in the database.
 * The main input is a {@link InAppNotificationCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link InAppNotificationDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class InAppNotificationQueryService extends QueryService<InAppNotification> {

    private static final Logger LOG = LoggerFactory.getLogger(InAppNotificationQueryService.class);

    private final InAppNotificationRepository inAppNotificationRepository;

    private final InAppNotificationMapper inAppNotificationMapper;

    public InAppNotificationQueryService(
        InAppNotificationRepository inAppNotificationRepository,
        InAppNotificationMapper inAppNotificationMapper
    ) {
        this.inAppNotificationRepository = inAppNotificationRepository;
        this.inAppNotificationMapper = inAppNotificationMapper;
    }

    /**
     * Return a {@link Page} of {@link InAppNotificationDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<InAppNotificationDTO> findByCriteria(InAppNotificationCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<InAppNotification> specification = createSpecification(criteria);
        return inAppNotificationRepository.findAll(specification, page).map(inAppNotificationMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(InAppNotificationCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<InAppNotification> specification = createSpecification(criteria);
        return inAppNotificationRepository.count(specification);
    }

    /**
     * Function to convert {@link InAppNotificationCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<InAppNotification> createSpecification(InAppNotificationCriteria criteria) {
        Specification<InAppNotification> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), InAppNotification_.id),
                buildStringSpecification(criteria.getTitle(), InAppNotification_.title),
                buildRangeSpecification(criteria.getCreatedAt(), InAppNotification_.createdAt),
                buildSpecification(criteria.getRead(), InAppNotification_.read),
                buildSpecification(criteria.getUserId(), root -> root.join(InAppNotification_.user, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
