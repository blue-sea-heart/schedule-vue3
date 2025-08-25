package com.blueseaheart.demo.web.rest;

import com.blueseaheart.demo.repository.ScheduleStatusRepository;
import com.blueseaheart.demo.service.ScheduleStatusQueryService;
import com.blueseaheart.demo.service.ScheduleStatusService;
import com.blueseaheart.demo.service.criteria.ScheduleStatusCriteria;
import com.blueseaheart.demo.service.dto.ScheduleStatusDTO;
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
 * REST controller for managing {@link com.blueseaheart.demo.domain.ScheduleStatus}.
 */
@RestController
@RequestMapping("/api/schedule-statuses")
public class ScheduleStatusResource {

    private static final Logger LOG = LoggerFactory.getLogger(ScheduleStatusResource.class);

    private static final String ENTITY_NAME = "scheduleStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ScheduleStatusService scheduleStatusService;

    private final ScheduleStatusRepository scheduleStatusRepository;

    private final ScheduleStatusQueryService scheduleStatusQueryService;

    public ScheduleStatusResource(
        ScheduleStatusService scheduleStatusService,
        ScheduleStatusRepository scheduleStatusRepository,
        ScheduleStatusQueryService scheduleStatusQueryService
    ) {
        this.scheduleStatusService = scheduleStatusService;
        this.scheduleStatusRepository = scheduleStatusRepository;
        this.scheduleStatusQueryService = scheduleStatusQueryService;
    }

    /**
     * {@code POST  /schedule-statuses} : Create a new scheduleStatus.
     *
     * @param scheduleStatusDTO the scheduleStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scheduleStatusDTO, or with status {@code 400 (Bad Request)} if the scheduleStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ScheduleStatusDTO> createScheduleStatus(@Valid @RequestBody ScheduleStatusDTO scheduleStatusDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ScheduleStatus : {}", scheduleStatusDTO);
        if (scheduleStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new scheduleStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        scheduleStatusDTO = scheduleStatusService.save(scheduleStatusDTO);
        return ResponseEntity.created(new URI("/api/schedule-statuses/" + scheduleStatusDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, scheduleStatusDTO.getId().toString()))
            .body(scheduleStatusDTO);
    }

    /**
     * {@code PUT  /schedule-statuses/:id} : Updates an existing scheduleStatus.
     *
     * @param id the id of the scheduleStatusDTO to save.
     * @param scheduleStatusDTO the scheduleStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleStatusDTO,
     * or with status {@code 400 (Bad Request)} if the scheduleStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scheduleStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScheduleStatusDTO> updateScheduleStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ScheduleStatusDTO scheduleStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ScheduleStatus : {}, {}", id, scheduleStatusDTO);
        if (scheduleStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        scheduleStatusDTO = scheduleStatusService.update(scheduleStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduleStatusDTO.getId().toString()))
            .body(scheduleStatusDTO);
    }

    /**
     * {@code PATCH  /schedule-statuses/:id} : Partial updates given fields of an existing scheduleStatus, field will ignore if it is null
     *
     * @param id the id of the scheduleStatusDTO to save.
     * @param scheduleStatusDTO the scheduleStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scheduleStatusDTO,
     * or with status {@code 400 (Bad Request)} if the scheduleStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the scheduleStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the scheduleStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScheduleStatusDTO> partialUpdateScheduleStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ScheduleStatusDTO scheduleStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ScheduleStatus partially : {}, {}", id, scheduleStatusDTO);
        if (scheduleStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scheduleStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scheduleStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScheduleStatusDTO> result = scheduleStatusService.partialUpdate(scheduleStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, scheduleStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /schedule-statuses} : get all the scheduleStatuses.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scheduleStatuses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ScheduleStatusDTO>> getAllScheduleStatuses(
        ScheduleStatusCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ScheduleStatuses by criteria: {}", criteria);

        Page<ScheduleStatusDTO> page = scheduleStatusQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /schedule-statuses/count} : count all the scheduleStatuses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countScheduleStatuses(ScheduleStatusCriteria criteria) {
        LOG.debug("REST request to count ScheduleStatuses by criteria: {}", criteria);
        return ResponseEntity.ok().body(scheduleStatusQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /schedule-statuses/:id} : get the "id" scheduleStatus.
     *
     * @param id the id of the scheduleStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scheduleStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScheduleStatusDTO> getScheduleStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ScheduleStatus : {}", id);
        Optional<ScheduleStatusDTO> scheduleStatusDTO = scheduleStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scheduleStatusDTO);
    }

    /**
     * {@code DELETE  /schedule-statuses/:id} : delete the "id" scheduleStatus.
     *
     * @param id the id of the scheduleStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScheduleStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ScheduleStatus : {}", id);
        scheduleStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
