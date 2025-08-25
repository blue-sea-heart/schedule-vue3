package com.blueseaheart.demo.service;

import com.blueseaheart.demo.domain.*; // for static metamodels
import com.blueseaheart.demo.domain.Schedule;
import com.blueseaheart.demo.repository.ScheduleRepository;
import com.blueseaheart.demo.service.criteria.ScheduleCriteria;
import com.blueseaheart.demo.service.dto.ScheduleDTO;
import com.blueseaheart.demo.service.mapper.ScheduleMapper;
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
 * Service for executing complex queries for {@link Schedule} entities in the database.
 * The main input is a {@link ScheduleCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ScheduleDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ScheduleQueryService extends QueryService<Schedule> {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleQueryService.class);

    private final ScheduleRepository scheduleRepository;

    private final ScheduleMapper scheduleMapper;

    public ScheduleQueryService(ScheduleRepository scheduleRepository, ScheduleMapper scheduleMapper) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleMapper = scheduleMapper;
    }

    /**
     * Return a {@link Page} of {@link ScheduleDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ScheduleDTO> findByCriteria(ScheduleCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Schedule> specification = createSpecification(criteria);
        return scheduleRepository.fetchBagRelationships(scheduleRepository.findAll(specification, page)).map(scheduleMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ScheduleCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Schedule> specification = createSpecification(criteria);
        return scheduleRepository.count(specification);
    }

    /**
     * Function to convert {@link ScheduleCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Schedule> createSpecification(ScheduleCriteria criteria) {
        Specification<Schedule> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Schedule_.id),
                buildStringSpecification(criteria.getTitle(), Schedule_.title),
                buildStringSpecification(criteria.getLocation(), Schedule_.location),
                buildSpecification(criteria.getAllDay(), Schedule_.allDay),
                buildRangeSpecification(criteria.getStartTime(), Schedule_.startTime),
                buildRangeSpecification(criteria.getEndTime(), Schedule_.endTime),
                buildSpecification(criteria.getPriority(), Schedule_.priority),
                buildRangeSpecification(criteria.getCompletedAt(), Schedule_.completedAt),
                buildSpecification(criteria.getVisibility(), Schedule_.visibility),
                buildSpecification(criteria.getRemindersId(), root -> root.join(Schedule_.reminders, JoinType.LEFT).get(Reminder_.id)),
                buildSpecification(criteria.getOwnerId(), root -> root.join(Schedule_.owner, JoinType.LEFT).get(User_.id)),
                buildSpecification(criteria.getStatusId(), root -> root.join(Schedule_.status, JoinType.LEFT).get(ScheduleStatus_.id)),
                buildSpecification(criteria.getCategoryId(), root -> root.join(Schedule_.category, JoinType.LEFT).get(Category_.id)),
                buildSpecification(criteria.getTagsId(), root -> root.join(Schedule_.tags, JoinType.LEFT).get(Tag_.id))
            );
        }
        return specification;
    }
}
