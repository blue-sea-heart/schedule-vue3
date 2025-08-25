package com.blueseaheart.demo.service;

import com.blueseaheart.demo.domain.ViewPreference;
import com.blueseaheart.demo.repository.ViewPreferenceRepository;
import com.blueseaheart.demo.service.dto.ViewPreferenceDTO;
import com.blueseaheart.demo.service.mapper.ViewPreferenceMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.blueseaheart.demo.domain.ViewPreference}.
 */
@Service
@Transactional
public class ViewPreferenceService {

    private static final Logger LOG = LoggerFactory.getLogger(ViewPreferenceService.class);

    private final ViewPreferenceRepository viewPreferenceRepository;

    private final ViewPreferenceMapper viewPreferenceMapper;

    public ViewPreferenceService(ViewPreferenceRepository viewPreferenceRepository, ViewPreferenceMapper viewPreferenceMapper) {
        this.viewPreferenceRepository = viewPreferenceRepository;
        this.viewPreferenceMapper = viewPreferenceMapper;
    }

    /**
     * Save a viewPreference.
     *
     * @param viewPreferenceDTO the entity to save.
     * @return the persisted entity.
     */
    public ViewPreferenceDTO save(ViewPreferenceDTO viewPreferenceDTO) {
        LOG.debug("Request to save ViewPreference : {}", viewPreferenceDTO);
        ViewPreference viewPreference = viewPreferenceMapper.toEntity(viewPreferenceDTO);
        viewPreference = viewPreferenceRepository.save(viewPreference);
        return viewPreferenceMapper.toDto(viewPreference);
    }

    /**
     * Update a viewPreference.
     *
     * @param viewPreferenceDTO the entity to save.
     * @return the persisted entity.
     */
    public ViewPreferenceDTO update(ViewPreferenceDTO viewPreferenceDTO) {
        LOG.debug("Request to update ViewPreference : {}", viewPreferenceDTO);
        ViewPreference viewPreference = viewPreferenceMapper.toEntity(viewPreferenceDTO);
        viewPreference = viewPreferenceRepository.save(viewPreference);
        return viewPreferenceMapper.toDto(viewPreference);
    }

    /**
     * Partially update a viewPreference.
     *
     * @param viewPreferenceDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ViewPreferenceDTO> partialUpdate(ViewPreferenceDTO viewPreferenceDTO) {
        LOG.debug("Request to partially update ViewPreference : {}", viewPreferenceDTO);

        return viewPreferenceRepository
            .findById(viewPreferenceDTO.getId())
            .map(existingViewPreference -> {
                viewPreferenceMapper.partialUpdate(existingViewPreference, viewPreferenceDTO);

                return existingViewPreference;
            })
            .map(viewPreferenceRepository::save)
            .map(viewPreferenceMapper::toDto);
    }

    /**
     * Get all the viewPreferences with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ViewPreferenceDTO> findAllWithEagerRelationships(Pageable pageable) {
        return viewPreferenceRepository.findAllWithEagerRelationships(pageable).map(viewPreferenceMapper::toDto);
    }

    /**
     * Get one viewPreference by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ViewPreferenceDTO> findOne(Long id) {
        LOG.debug("Request to get ViewPreference : {}", id);
        return viewPreferenceRepository.findOneWithEagerRelationships(id).map(viewPreferenceMapper::toDto);
    }

    /**
     * Delete the viewPreference by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ViewPreference : {}", id);
        viewPreferenceRepository.deleteById(id);
    }
}
