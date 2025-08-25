package com.blueseaheart.demo.web.rest;

import com.blueseaheart.demo.repository.ViewPreferenceRepository;
import com.blueseaheart.demo.service.ViewPreferenceQueryService;
import com.blueseaheart.demo.service.ViewPreferenceService;
import com.blueseaheart.demo.service.criteria.ViewPreferenceCriteria;
import com.blueseaheart.demo.service.dto.ViewPreferenceDTO;
import com.blueseaheart.demo.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.blueseaheart.demo.domain.ViewPreference}.
 */
@RestController
@RequestMapping("/api/view-preferences")
public class ViewPreferenceResource {

    private static final Logger LOG = LoggerFactory.getLogger(ViewPreferenceResource.class);

    private static final String ENTITY_NAME = "viewPreference";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ViewPreferenceService viewPreferenceService;

    private final ViewPreferenceRepository viewPreferenceRepository;

    private final ViewPreferenceQueryService viewPreferenceQueryService;

    public ViewPreferenceResource(
        ViewPreferenceService viewPreferenceService,
        ViewPreferenceRepository viewPreferenceRepository,
        ViewPreferenceQueryService viewPreferenceQueryService
    ) {
        this.viewPreferenceService = viewPreferenceService;
        this.viewPreferenceRepository = viewPreferenceRepository;
        this.viewPreferenceQueryService = viewPreferenceQueryService;
    }

    /**
     * {@code POST  /view-preferences} : Create a new viewPreference.
     *
     * @param viewPreferenceDTO the viewPreferenceDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new viewPreferenceDTO, or with status {@code 400 (Bad Request)} if the viewPreference has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ViewPreferenceDTO> createViewPreference(@Valid @RequestBody ViewPreferenceDTO viewPreferenceDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ViewPreference : {}", viewPreferenceDTO);
        if (viewPreferenceDTO.getId() != null) {
            throw new BadRequestAlertException("A new viewPreference cannot already have an ID", ENTITY_NAME, "idexists");
        }
        viewPreferenceDTO = viewPreferenceService.save(viewPreferenceDTO);
        return ResponseEntity.created(new URI("/api/view-preferences/" + viewPreferenceDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, viewPreferenceDTO.getId().toString()))
            .body(viewPreferenceDTO);
    }

    /**
     * {@code PUT  /view-preferences/:id} : Updates an existing viewPreference.
     *
     * @param id the id of the viewPreferenceDTO to save.
     * @param viewPreferenceDTO the viewPreferenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewPreferenceDTO,
     * or with status {@code 400 (Bad Request)} if the viewPreferenceDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the viewPreferenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ViewPreferenceDTO> updateViewPreference(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ViewPreferenceDTO viewPreferenceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ViewPreference : {}, {}", id, viewPreferenceDTO);
        if (viewPreferenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viewPreferenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!viewPreferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        viewPreferenceDTO = viewPreferenceService.update(viewPreferenceDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, viewPreferenceDTO.getId().toString()))
            .body(viewPreferenceDTO);
    }

    /**
     * {@code PATCH  /view-preferences/:id} : Partial updates given fields of an existing viewPreference, field will ignore if it is null
     *
     * @param id the id of the viewPreferenceDTO to save.
     * @param viewPreferenceDTO the viewPreferenceDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated viewPreferenceDTO,
     * or with status {@code 400 (Bad Request)} if the viewPreferenceDTO is not valid,
     * or with status {@code 404 (Not Found)} if the viewPreferenceDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the viewPreferenceDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ViewPreferenceDTO> partialUpdateViewPreference(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ViewPreferenceDTO viewPreferenceDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ViewPreference partially : {}, {}", id, viewPreferenceDTO);
        if (viewPreferenceDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, viewPreferenceDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!viewPreferenceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ViewPreferenceDTO> result = viewPreferenceService.partialUpdate(viewPreferenceDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, viewPreferenceDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /view-preferences} : get all the viewPreferences.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of viewPreferences in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ViewPreferenceDTO>> getAllViewPreferences(
        ViewPreferenceCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ViewPreferences by criteria: {}", criteria);

        Page<ViewPreferenceDTO> page = viewPreferenceQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /view-preferences/count} : count all the viewPreferences.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countViewPreferences(ViewPreferenceCriteria criteria) {
        LOG.debug("REST request to count ViewPreferences by criteria: {}", criteria);
        return ResponseEntity.ok().body(viewPreferenceQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /view-preferences/:id} : get the "id" viewPreference.
     *
     * @param id the id of the viewPreferenceDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the viewPreferenceDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ViewPreferenceDTO> getViewPreference(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ViewPreference : {}", id);
        Optional<ViewPreferenceDTO> viewPreferenceDTO = viewPreferenceService.findOne(id);
        return ResponseUtil.wrapOrNotFound(viewPreferenceDTO);
    }

    /**
     * {@code DELETE  /view-preferences/:id} : delete the "id" viewPreference.
     *
     * @param id the id of the viewPreferenceDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteViewPreference(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ViewPreference : {}", id);
        viewPreferenceService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
