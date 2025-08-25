package com.blueseaheart.demo.web.rest;

import com.blueseaheart.demo.repository.InAppNotificationRepository;
import com.blueseaheart.demo.service.InAppNotificationQueryService;
import com.blueseaheart.demo.service.InAppNotificationService;
import com.blueseaheart.demo.service.criteria.InAppNotificationCriteria;
import com.blueseaheart.demo.service.dto.InAppNotificationDTO;
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
 * REST controller for managing {@link com.blueseaheart.demo.domain.InAppNotification}.
 */
@RestController
@RequestMapping("/api/in-app-notifications")
public class InAppNotificationResource {

    private static final Logger LOG = LoggerFactory.getLogger(InAppNotificationResource.class);

    private static final String ENTITY_NAME = "inAppNotification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InAppNotificationService inAppNotificationService;

    private final InAppNotificationRepository inAppNotificationRepository;

    private final InAppNotificationQueryService inAppNotificationQueryService;

    public InAppNotificationResource(
        InAppNotificationService inAppNotificationService,
        InAppNotificationRepository inAppNotificationRepository,
        InAppNotificationQueryService inAppNotificationQueryService
    ) {
        this.inAppNotificationService = inAppNotificationService;
        this.inAppNotificationRepository = inAppNotificationRepository;
        this.inAppNotificationQueryService = inAppNotificationQueryService;
    }

    /**
     * {@code POST  /in-app-notifications} : Create a new inAppNotification.
     *
     * @param inAppNotificationDTO the inAppNotificationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new inAppNotificationDTO, or with status {@code 400 (Bad Request)} if the inAppNotification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InAppNotificationDTO> createInAppNotification(@Valid @RequestBody InAppNotificationDTO inAppNotificationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save InAppNotification : {}", inAppNotificationDTO);
        if (inAppNotificationDTO.getId() != null) {
            throw new BadRequestAlertException("A new inAppNotification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        inAppNotificationDTO = inAppNotificationService.save(inAppNotificationDTO);
        return ResponseEntity.created(new URI("/api/in-app-notifications/" + inAppNotificationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, inAppNotificationDTO.getId().toString()))
            .body(inAppNotificationDTO);
    }

    /**
     * {@code PUT  /in-app-notifications/:id} : Updates an existing inAppNotification.
     *
     * @param id the id of the inAppNotificationDTO to save.
     * @param inAppNotificationDTO the inAppNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inAppNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the inAppNotificationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the inAppNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InAppNotificationDTO> updateInAppNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InAppNotificationDTO inAppNotificationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update InAppNotification : {}, {}", id, inAppNotificationDTO);
        if (inAppNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inAppNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inAppNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        inAppNotificationDTO = inAppNotificationService.update(inAppNotificationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inAppNotificationDTO.getId().toString()))
            .body(inAppNotificationDTO);
    }

    /**
     * {@code PATCH  /in-app-notifications/:id} : Partial updates given fields of an existing inAppNotification, field will ignore if it is null
     *
     * @param id the id of the inAppNotificationDTO to save.
     * @param inAppNotificationDTO the inAppNotificationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated inAppNotificationDTO,
     * or with status {@code 400 (Bad Request)} if the inAppNotificationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the inAppNotificationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the inAppNotificationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InAppNotificationDTO> partialUpdateInAppNotification(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InAppNotificationDTO inAppNotificationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InAppNotification partially : {}, {}", id, inAppNotificationDTO);
        if (inAppNotificationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, inAppNotificationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!inAppNotificationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InAppNotificationDTO> result = inAppNotificationService.partialUpdate(inAppNotificationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, inAppNotificationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /in-app-notifications} : get all the inAppNotifications.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of inAppNotifications in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InAppNotificationDTO>> getAllInAppNotifications(
        InAppNotificationCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get InAppNotifications by criteria: {}", criteria);

        Page<InAppNotificationDTO> page = inAppNotificationQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /in-app-notifications/count} : count all the inAppNotifications.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countInAppNotifications(InAppNotificationCriteria criteria) {
        LOG.debug("REST request to count InAppNotifications by criteria: {}", criteria);
        return ResponseEntity.ok().body(inAppNotificationQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /in-app-notifications/:id} : get the "id" inAppNotification.
     *
     * @param id the id of the inAppNotificationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the inAppNotificationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InAppNotificationDTO> getInAppNotification(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InAppNotification : {}", id);
        Optional<InAppNotificationDTO> inAppNotificationDTO = inAppNotificationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(inAppNotificationDTO);
    }

    /**
     * {@code DELETE  /in-app-notifications/:id} : delete the "id" inAppNotification.
     *
     * @param id the id of the inAppNotificationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInAppNotification(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InAppNotification : {}", id);
        inAppNotificationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
