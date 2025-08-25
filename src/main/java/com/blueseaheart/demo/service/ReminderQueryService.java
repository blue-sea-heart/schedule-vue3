package com.blueseaheart.demo.service;

import com.blueseaheart.demo.domain.*; // for static metamodels
import com.blueseaheart.demo.domain.Reminder;
import com.blueseaheart.demo.repository.ReminderRepository;
import com.blueseaheart.demo.service.criteria.ReminderCriteria;
import com.blueseaheart.demo.service.dto.ReminderDTO;
import com.blueseaheart.demo.service.mapper.ReminderMapper;
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
 * Service for executing complex queries for {@link Reminder} entities in the database.
 * The main input is a {@link ReminderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ReminderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ReminderQueryService extends QueryService<Reminder> {

    private static final Logger LOG = LoggerFactory.getLogger(ReminderQueryService.class);

    private final ReminderRepository reminderRepository;

    private final ReminderMapper reminderMapper;

    public ReminderQueryService(ReminderRepository reminderRepository, ReminderMapper reminderMapper) {
        this.reminderRepository = reminderRepository;
        this.reminderMapper = reminderMapper;
    }

    /**
     * Return a {@link Page} of {@link ReminderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ReminderDTO> findByCriteria(ReminderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Reminder> specification = createSpecification(criteria);
        return reminderRepository.findAll(specification, page).map(reminderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ReminderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<Reminder> specification = createSpecification(criteria);
        return reminderRepository.count(specification);
    }

    /**
     * Function to convert {@link ReminderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Reminder> createSpecification(ReminderCriteria criteria) {
        Specification<Reminder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), Reminder_.id),
                buildRangeSpecification(criteria.getRemindAt(), Reminder_.remindAt),
                buildSpecification(criteria.getChannel(), Reminder_.channel),
                buildStringSpecification(criteria.getSubject(), Reminder_.subject),
                buildSpecification(criteria.getSent(), Reminder_.sent),
                buildRangeSpecification(criteria.getAttemptCount(), Reminder_.attemptCount),
                buildRangeSpecification(criteria.getLastAttemptAt(), Reminder_.lastAttemptAt),
                buildStringSpecification(criteria.getLastErrorMsg(), Reminder_.lastErrorMsg),
                buildStringSpecification(criteria.getErrorMsg(), Reminder_.errorMsg),
                buildSpecification(criteria.getScheduleId(), root -> root.join(Reminder_.schedule, JoinType.LEFT).get(Schedule_.id))
            );
        }
        return specification;
    }
}
