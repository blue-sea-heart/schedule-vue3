package com.blueseaheart.demo.service;

import com.blueseaheart.demo.domain.InAppNotification;
import com.blueseaheart.demo.repository.InAppNotificationRepository;
import com.blueseaheart.demo.service.dto.InAppNotificationDTO;
import com.blueseaheart.demo.service.mapper.InAppNotificationMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blueseaheart.demo.domain.InAppNotification}.
 */
@Service
@Transactional
public class InAppNotificationService {

    private static final Logger LOG = LoggerFactory.getLogger(InAppNotificationService.class);

    private final InAppNotificationRepository inAppNotificationRepository;

    private final InAppNotificationMapper inAppNotificationMapper;

    public InAppNotificationService(
        InAppNotificationRepository inAppNotificationRepository,
        InAppNotificationMapper inAppNotificationMapper
    ) {
        this.inAppNotificationRepository = inAppNotificationRepository;
        this.inAppNotificationMapper = inAppNotificationMapper;
    }

    /**
     * Save a inAppNotification.
     *
     * @param inAppNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    public InAppNotificationDTO save(InAppNotificationDTO inAppNotificationDTO) {
        LOG.debug("Request to save InAppNotification : {}", inAppNotificationDTO);
        InAppNotification inAppNotification = inAppNotificationMapper.toEntity(inAppNotificationDTO);
        inAppNotification = inAppNotificationRepository.save(inAppNotification);
        return inAppNotificationMapper.toDto(inAppNotification);
    }

    /**
     * Update a inAppNotification.
     *
     * @param inAppNotificationDTO the entity to save.
     * @return the persisted entity.
     */
    public InAppNotificationDTO update(InAppNotificationDTO inAppNotificationDTO) {
        LOG.debug("Request to update InAppNotification : {}", inAppNotificationDTO);
        InAppNotification inAppNotification = inAppNotificationMapper.toEntity(inAppNotificationDTO);
        inAppNotification = inAppNotificationRepository.save(inAppNotification);
        return inAppNotificationMapper.toDto(inAppNotification);
    }

    /**
     * Partially update a inAppNotification.
     *
     * @param inAppNotificationDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<InAppNotificationDTO> partialUpdate(InAppNotificationDTO inAppNotificationDTO) {
        LOG.debug("Request to partially update InAppNotification : {}", inAppNotificationDTO);

        return inAppNotificationRepository
            .findById(inAppNotificationDTO.getId())
            .map(existingInAppNotification -> {
                inAppNotificationMapper.partialUpdate(existingInAppNotification, inAppNotificationDTO);

                return existingInAppNotification;
            })
            .map(inAppNotificationRepository::save)
            .map(inAppNotificationMapper::toDto);
    }

    /**
     * Get all the inAppNotifications with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<InAppNotificationDTO> findAllWithEagerRelationships(Pageable pageable) {
        return inAppNotificationRepository.findAllWithEagerRelationships(pageable).map(inAppNotificationMapper::toDto);
    }

    /**
     * Get one inAppNotification by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<InAppNotificationDTO> findOne(Long id) {
        LOG.debug("Request to get InAppNotification : {}", id);
        return inAppNotificationRepository.findOneWithEagerRelationships(id).map(inAppNotificationMapper::toDto);
    }

    /**
     * Delete the inAppNotification by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete InAppNotification : {}", id);
        inAppNotificationRepository.deleteById(id);
    }
}
