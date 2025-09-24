package com.blueseaheart.demo.service;

import com.blueseaheart.demo.domain.Schedule;
import com.blueseaheart.demo.domain.ScheduleStatus;
import com.blueseaheart.demo.repository.ScheduleRepository;
import com.blueseaheart.demo.repository.ScheduleStatusRepository;
import com.blueseaheart.demo.service.dto.ScheduleDTO;
import com.blueseaheart.demo.service.mapper.ScheduleMapper;
import com.blueseaheart.demo.web.rest.errors.BusinessException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blueseaheart.demo.domain.Schedule}.
 */
@Service
@Transactional
public class ScheduleService {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleService.class);

    private final ScheduleRepository scheduleRepository;

    private final ScheduleMapper scheduleMapper;

    private final ScheduleStatusRepository statusRepository;

    public ScheduleService(
        ScheduleRepository scheduleRepository,
        ScheduleMapper scheduleMapper,
        ScheduleStatusRepository statusRepository
    ) {
        this.scheduleRepository = scheduleRepository;
        this.scheduleMapper = scheduleMapper;
        this.statusRepository = statusRepository;
    }

    /**
     * Save a schedule.
     *
     * @param scheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public ScheduleDTO save(ScheduleDTO scheduleDTO) {
        LOG.debug("Request to save Schedule : {}", scheduleDTO);
        Schedule schedule = scheduleMapper.toEntity(scheduleDTO);
        var status = resolveStatus(scheduleDTO.getStatus().getCode(), scheduleDTO.getStatus().getId());
        if (status.isEmpty()) {
            throw new BusinessException("schedule.status.notFound", "所选状态不存在，请联系管理员。");
        }
        schedule.setStatus(status.get());
        if (Boolean.TRUE.equals(schedule.getStatus().getIsTerminal())) {
            throw new BusinessException("schedule.status.terminalOnCreate", "不能用终止状态创建日程。");
        }
        if (
            Boolean.TRUE.equals(schedule.getAllDay()) &&
            schedule.getEndTime() != null &&
            schedule.getEndTime().isBefore(schedule.getStartTime())
        ) {
            throw new BusinessException("schedule.time.invalidRange", "结束时间不能早于开始时间。");
        }

        schedule = scheduleRepository.save(schedule);
        return scheduleMapper.toDto(schedule);
    }

    /**
     * Update a schedule.
     *
     * @param scheduleDTO the entity to save.
     * @return the persisted entity.
     */
    public ScheduleDTO update(ScheduleDTO scheduleDTO) {
        LOG.debug("Request to update Schedule : {}", scheduleDTO);
        Schedule entity = scheduleRepository
            .findById(scheduleDTO.getId())
            .orElseThrow(() -> new BusinessException("schedule.notFound", "日程不存在。"));
        scheduleMapper.partialUpdate(entity, scheduleDTO);

        if (scheduleDTO.getStatus() != null && (scheduleDTO.getStatus().getCode() != null || scheduleDTO.getStatus().getId() != null)) {
            ScheduleStatus status = resolveStatus(scheduleDTO.getStatus().getCode(), scheduleDTO.getStatus().getId()).orElseThrow(() ->
                new BusinessException("schedule.status.notFound", "所选状态不存在，请联系管理员。")
            );
            entity.setStatus(status);
        }

        // 若切换到终止态，自动回填完成时间
        if (Boolean.TRUE.equals(entity.getStatus().getIsTerminal()) && entity.getCompletedAt() == null) {
            entity.setCompletedAt(java.time.ZonedDateTime.now(java.time.ZoneId.systemDefault()));
        }

        return scheduleMapper.toDto(scheduleRepository.save(entity));
    }

    /**
     * Partially update a schedule.
     *
     * @param scheduleDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ScheduleDTO> partialUpdate(ScheduleDTO scheduleDTO) {
        LOG.debug("Request to partially update Schedule : {}", scheduleDTO);

        return scheduleRepository
            .findById(scheduleDTO.getId())
            .map(existingSchedule -> {
                scheduleMapper.partialUpdate(existingSchedule, scheduleDTO);

                return existingSchedule;
            })
            .map(scheduleRepository::save)
            .map(scheduleMapper::toDto);
    }

    /**
     * Get all the schedules with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ScheduleDTO> findAllWithEagerRelationships(Pageable pageable) {
        return scheduleRepository.findAllWithEagerRelationships(pageable).map(scheduleMapper::toDto);
    }

    /**
     * Get one schedule by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ScheduleDTO> findOne(Long id) {
        LOG.debug("Request to get Schedule : {}", id);
        return scheduleRepository.findOneWithEagerRelationships(id).map(scheduleMapper::toDto);
    }

    /**
     * Delete the schedule by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Schedule : {}", id);
        scheduleRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    Optional<ScheduleStatus> resolveStatus(String code, Long id) {
        if (code != null && !code.isBlank()) return statusRepository.findByCode(code);
        if (id != null) return statusRepository.findById(id);
        return statusRepository.findFirstByIsDefaultTrue();
    }
}
