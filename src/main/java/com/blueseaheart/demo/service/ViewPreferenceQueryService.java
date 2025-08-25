package com.blueseaheart.demo.service;

import com.blueseaheart.demo.domain.*; // for static metamodels
import com.blueseaheart.demo.domain.ViewPreference;
import com.blueseaheart.demo.repository.ViewPreferenceRepository;
import com.blueseaheart.demo.service.criteria.ViewPreferenceCriteria;
import com.blueseaheart.demo.service.dto.ViewPreferenceDTO;
import com.blueseaheart.demo.service.mapper.ViewPreferenceMapper;
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
 * Service for executing complex queries for {@link ViewPreference} entities in the database.
 * The main input is a {@link ViewPreferenceCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ViewPreferenceDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ViewPreferenceQueryService extends QueryService<ViewPreference> {

    private static final Logger LOG = LoggerFactory.getLogger(ViewPreferenceQueryService.class);

    private final ViewPreferenceRepository viewPreferenceRepository;

    private final ViewPreferenceMapper viewPreferenceMapper;

    public ViewPreferenceQueryService(ViewPreferenceRepository viewPreferenceRepository, ViewPreferenceMapper viewPreferenceMapper) {
        this.viewPreferenceRepository = viewPreferenceRepository;
        this.viewPreferenceMapper = viewPreferenceMapper;
    }

    /**
     * Return a {@link Page} of {@link ViewPreferenceDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ViewPreferenceDTO> findByCriteria(ViewPreferenceCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ViewPreference> specification = createSpecification(criteria);
        return viewPreferenceRepository.findAll(specification, page).map(viewPreferenceMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ViewPreferenceCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ViewPreference> specification = createSpecification(criteria);
        return viewPreferenceRepository.count(specification);
    }

    /**
     * Function to convert {@link ViewPreferenceCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ViewPreference> createSpecification(ViewPreferenceCriteria criteria) {
        Specification<ViewPreference> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ViewPreference_.id),
                buildSpecification(criteria.getDefaultView(), ViewPreference_.defaultView),
                buildRangeSpecification(criteria.getLastStart(), ViewPreference_.lastStart),
                buildRangeSpecification(criteria.getLastEnd(), ViewPreference_.lastEnd),
                buildSpecification(criteria.getWeekStart(), ViewPreference_.weekStart),
                buildSpecification(criteria.getShowWeekend(), ViewPreference_.showWeekend),
                buildSpecification(criteria.getUserId(), root -> root.join(ViewPreference_.user, JoinType.LEFT).get(User_.id))
            );
        }
        return specification;
    }
}
