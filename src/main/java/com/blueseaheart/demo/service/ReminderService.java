package com.blueseaheart.demo.service;

import com.blueseaheart.demo.domain.Reminder;
import com.blueseaheart.demo.repository.ReminderRepository;
import com.blueseaheart.demo.service.dto.ReminderDTO;
import com.blueseaheart.demo.service.mapper.ReminderMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blueseaheart.demo.domain.Reminder}.
 */
@Service
@Transactional
public class ReminderService {

    private static final Logger LOG = LoggerFactory.getLogger(ReminderService.class);

    private final ReminderRepository reminderRepository;

    private final ReminderMapper reminderMapper;

    public ReminderService(ReminderRepository reminderRepository, ReminderMapper reminderMapper) {
        this.reminderRepository = reminderRepository;
        this.reminderMapper = reminderMapper;
    }

    /**
     * Save a reminder.
     *
     * @param reminderDTO the entity to save.
     * @return the persisted entity.
     */
    public ReminderDTO save(ReminderDTO reminderDTO) {
        LOG.debug("Request to save Reminder : {}", reminderDTO);
        Reminder reminder = reminderMapper.toEntity(reminderDTO);
        reminder = reminderRepository.save(reminder);
        return reminderMapper.toDto(reminder);
    }

    /**
     * Update a reminder.
     *
     * @param reminderDTO the entity to save.
     * @return the persisted entity.
     */
    public ReminderDTO update(ReminderDTO reminderDTO) {
        LOG.debug("Request to update Reminder : {}", reminderDTO);
        Reminder reminder = reminderMapper.toEntity(reminderDTO);
        reminder = reminderRepository.save(reminder);
        return reminderMapper.toDto(reminder);
    }

    /**
     * Partially update a reminder.
     *
     * @param reminderDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ReminderDTO> partialUpdate(ReminderDTO reminderDTO) {
        LOG.debug("Request to partially update Reminder : {}", reminderDTO);

        return reminderRepository
            .findById(reminderDTO.getId())
            .map(existingReminder -> {
                reminderMapper.partialUpdate(existingReminder, reminderDTO);

                return existingReminder;
            })
            .map(reminderRepository::save)
            .map(reminderMapper::toDto);
    }

    /**
     * Get one reminder by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ReminderDTO> findOne(Long id) {
        LOG.debug("Request to get Reminder : {}", id);
        return reminderRepository.findById(id).map(reminderMapper::toDto);
    }

    /**
     * Delete the reminder by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Reminder : {}", id);
        reminderRepository.deleteById(id);
    }
}
