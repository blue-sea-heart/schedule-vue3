package com.blueseaheart.demo.service;

import com.blueseaheart.demo.domain.*; // for static metamodels
import com.blueseaheart.demo.domain.ScheduleStatus;
import com.blueseaheart.demo.repository.ScheduleStatusRepository;
import com.blueseaheart.demo.service.criteria.ScheduleStatusCriteria;
import com.blueseaheart.demo.service.dto.ScheduleStatusDTO;
import com.blueseaheart.demo.service.mapper.ScheduleStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ScheduleStatus} entities in the database.
 * The main input is a {@link ScheduleStatusCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ScheduleStatusDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ScheduleStatusQueryService extends QueryService<ScheduleStatus> {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleStatusQueryService.class);

    private final ScheduleStatusRepository scheduleStatusRepository;

    private final ScheduleStatusMapper scheduleStatusMapper;

    public ScheduleStatusQueryService(ScheduleStatusRepository scheduleStatusRepository, ScheduleStatusMapper scheduleStatusMapper) {
        this.scheduleStatusRepository = scheduleStatusRepository;
        this.scheduleStatusMapper = scheduleStatusMapper;
    }

    /**
     * Return a {@link Page} of {@link ScheduleStatusDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ScheduleStatusDTO> findByCriteria(ScheduleStatusCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ScheduleStatus> specification = createSpecification(criteria);
        return scheduleStatusRepository.findAll(specification, page).map(scheduleStatusMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ScheduleStatusCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ScheduleStatus> specification = createSpecification(criteria);
        return scheduleStatusRepository.count(specification);
    }

    /**
     * Function to convert {@link ScheduleStatusCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ScheduleStatus> createSpecification(ScheduleStatusCriteria criteria) {
        Specification<ScheduleStatus> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ScheduleStatus_.id),
                buildStringSpecification(criteria.getCode(), ScheduleStatus_.code),
                buildStringSpecification(criteria.getName(), ScheduleStatus_.name),
                buildStringSpecification(criteria.getColor(), ScheduleStatus_.color),
                buildRangeSpecification(criteria.getSortNo(), ScheduleStatus_.sortNo),
                buildSpecification(criteria.getIsDefault(), ScheduleStatus_.isDefault),
                buildSpecification(criteria.getIsTerminal(), ScheduleStatus_.isTerminal)
            );
        }
        return specification;
    }
}
