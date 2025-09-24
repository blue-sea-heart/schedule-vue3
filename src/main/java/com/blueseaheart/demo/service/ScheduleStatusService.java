package com.blueseaheart.demo.service;

import com.blueseaheart.demo.domain.ScheduleStatus;
import com.blueseaheart.demo.repository.ScheduleStatusRepository;
import com.blueseaheart.demo.service.dto.ScheduleStatusDTO;
import com.blueseaheart.demo.service.mapper.ScheduleStatusMapper;
import com.blueseaheart.demo.web.rest.errors.BusinessException;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blueseaheart.demo.domain.ScheduleStatus}.
 */
@Service
@Transactional
public class ScheduleStatusService {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleStatusService.class);

    private final ScheduleStatusRepository scheduleStatusRepository;

    private final ScheduleStatusMapper scheduleStatusMapper;

    public ScheduleStatusService(ScheduleStatusRepository scheduleStatusRepository, ScheduleStatusMapper scheduleStatusMapper) {
        this.scheduleStatusRepository = scheduleStatusRepository;
        this.scheduleStatusMapper = scheduleStatusMapper;
    }

    /**
     * Save a scheduleStatus.
     *
     * @param scheduleStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public ScheduleStatusDTO save(ScheduleStatusDTO scheduleStatusDTO) {
        LOG.debug("Request to save ScheduleStatus : {}", scheduleStatusDTO);
        ScheduleStatus scheduleStatus = scheduleStatusMapper.toEntity(scheduleStatusDTO);
        scheduleStatus = scheduleStatusRepository.save(scheduleStatus);
        return scheduleStatusMapper.toDto(scheduleStatus);
    }

    /**
     * Update a scheduleStatus.
     *
     * @param scheduleStatusDTO the entity to save.
     * @return the persisted entity.
     */
    public ScheduleStatusDTO update(ScheduleStatusDTO scheduleStatusDTO) {
        LOG.debug("Request to update ScheduleStatus : {}", scheduleStatusDTO);
        ScheduleStatus scheduleStatus = scheduleStatusMapper.toEntity(scheduleStatusDTO);
        scheduleStatus = scheduleStatusRepository.save(scheduleStatus);
        return scheduleStatusMapper.toDto(scheduleStatus);
    }

    /**
     * Partially update a scheduleStatus.
     *
     * @param scheduleStatusDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ScheduleStatusDTO> partialUpdate(ScheduleStatusDTO scheduleStatusDTO) {
        LOG.debug("Request to partially update ScheduleStatus : {}", scheduleStatusDTO);

        return scheduleStatusRepository
            .findById(scheduleStatusDTO.getId())
            .map(existingScheduleStatus -> {
                scheduleStatusMapper.partialUpdate(existingScheduleStatus, scheduleStatusDTO);

                return existingScheduleStatus;
            })
            .map(scheduleStatusRepository::save)
            .map(scheduleStatusMapper::toDto);
    }

    /**
     * Get one scheduleStatus by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ScheduleStatusDTO> findOne(Long id) {
        LOG.debug("Request to get ScheduleStatus : {}", id);
        return scheduleStatusRepository.findById(id).map(scheduleStatusMapper::toDto);
    }

    @Transactional(readOnly = true)
    public Optional<ScheduleStatusDTO> findOneByCode(String code) {
        return scheduleStatusRepository.findByCode(code).map(scheduleStatusMapper::toDto);
    }

    @Transactional(readOnly = true)
    public ScheduleStatus requireByCode(String code) {
        return scheduleStatusRepository
            .findByCode(code)
            .orElseThrow(() -> new BusinessException("status.notFound", "所选状态不存在，请联系管理员。"));
    }

    @Transactional(readOnly = true)
    public Optional<ScheduleStatusDTO> findDefault() {
        return scheduleStatusRepository.findFirstByIsDefaultTrue().map(scheduleStatusMapper::toDto);
    }

    /**
     * Delete the scheduleStatus by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ScheduleStatus : {}", id);
        scheduleStatusRepository.deleteById(id);
    }
}
