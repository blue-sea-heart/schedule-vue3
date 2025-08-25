package com.blueseaheart.demo.web.rest;

import static com.blueseaheart.demo.domain.ScheduleStatusAsserts.*;
import static com.blueseaheart.demo.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blueseaheart.demo.IntegrationTest;
import com.blueseaheart.demo.domain.ScheduleStatus;
import com.blueseaheart.demo.repository.ScheduleStatusRepository;
import com.blueseaheart.demo.service.dto.ScheduleStatusDTO;
import com.blueseaheart.demo.service.mapper.ScheduleStatusMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ScheduleStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScheduleStatusResourceIT {

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final Integer DEFAULT_SORT_NO = 1;
    private static final Integer UPDATED_SORT_NO = 2;
    private static final Integer SMALLER_SORT_NO = 1 - 1;

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final Boolean DEFAULT_IS_TERMINAL = false;
    private static final Boolean UPDATED_IS_TERMINAL = true;

    private static final String ENTITY_API_URL = "/api/schedule-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScheduleStatusRepository scheduleStatusRepository;

    @Autowired
    private ScheduleStatusMapper scheduleStatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduleStatusMockMvc;

    private ScheduleStatus scheduleStatus;

    private ScheduleStatus insertedScheduleStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleStatus createEntity() {
        return new ScheduleStatus()
            .code(DEFAULT_CODE)
            .name(DEFAULT_NAME)
            .color(DEFAULT_COLOR)
            .sortNo(DEFAULT_SORT_NO)
            .isDefault(DEFAULT_IS_DEFAULT)
            .isTerminal(DEFAULT_IS_TERMINAL);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduleStatus createUpdatedEntity() {
        return new ScheduleStatus()
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .color(UPDATED_COLOR)
            .sortNo(UPDATED_SORT_NO)
            .isDefault(UPDATED_IS_DEFAULT)
            .isTerminal(UPDATED_IS_TERMINAL);
    }

    @BeforeEach
    void initTest() {
        scheduleStatus = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedScheduleStatus != null) {
            scheduleStatusRepository.delete(insertedScheduleStatus);
            insertedScheduleStatus = null;
        }
    }

    @Test
    @Transactional
    void createScheduleStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ScheduleStatus
        ScheduleStatusDTO scheduleStatusDTO = scheduleStatusMapper.toDto(scheduleStatus);
        var returnedScheduleStatusDTO = om.readValue(
            restScheduleStatusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleStatusDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ScheduleStatusDTO.class
        );

        // Validate the ScheduleStatus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedScheduleStatus = scheduleStatusMapper.toEntity(returnedScheduleStatusDTO);
        assertScheduleStatusUpdatableFieldsEquals(returnedScheduleStatus, getPersistedScheduleStatus(returnedScheduleStatus));

        insertedScheduleStatus = returnedScheduleStatus;
    }

    @Test
    @Transactional
    void createScheduleStatusWithExistingId() throws Exception {
        // Create the ScheduleStatus with an existing ID
        scheduleStatus.setId(1L);
        ScheduleStatusDTO scheduleStatusDTO = scheduleStatusMapper.toDto(scheduleStatus);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ScheduleStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduleStatus.setCode(null);

        // Create the ScheduleStatus, which fails.
        ScheduleStatusDTO scheduleStatusDTO = scheduleStatusMapper.toDto(scheduleStatus);

        restScheduleStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduleStatus.setName(null);

        // Create the ScheduleStatus, which fails.
        ScheduleStatusDTO scheduleStatusDTO = scheduleStatusMapper.toDto(scheduleStatus);

        restScheduleStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSortNoIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduleStatus.setSortNo(null);

        // Create the ScheduleStatus, which fails.
        ScheduleStatusDTO scheduleStatusDTO = scheduleStatusMapper.toDto(scheduleStatus);

        restScheduleStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDefaultIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduleStatus.setIsDefault(null);

        // Create the ScheduleStatus, which fails.
        ScheduleStatusDTO scheduleStatusDTO = scheduleStatusMapper.toDto(scheduleStatus);

        restScheduleStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsTerminalIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduleStatus.setIsTerminal(null);

        // Create the ScheduleStatus, which fails.
        ScheduleStatusDTO scheduleStatusDTO = scheduleStatusMapper.toDto(scheduleStatus);

        restScheduleStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllScheduleStatuses() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList
        restScheduleStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].sortNo").value(hasItem(DEFAULT_SORT_NO)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)))
            .andExpect(jsonPath("$.[*].isTerminal").value(hasItem(DEFAULT_IS_TERMINAL)));
    }

    @Test
    @Transactional
    void getScheduleStatus() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get the scheduleStatus
        restScheduleStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, scheduleStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scheduleStatus.getId().intValue()))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.sortNo").value(DEFAULT_SORT_NO))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT))
            .andExpect(jsonPath("$.isTerminal").value(DEFAULT_IS_TERMINAL));
    }

    @Test
    @Transactional
    void getScheduleStatusesByIdFiltering() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        Long id = scheduleStatus.getId();

        defaultScheduleStatusFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultScheduleStatusFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultScheduleStatusFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where code equals to
        defaultScheduleStatusFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where code in
        defaultScheduleStatusFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where code is not null
        defaultScheduleStatusFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where code contains
        defaultScheduleStatusFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where code does not contain
        defaultScheduleStatusFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where name equals to
        defaultScheduleStatusFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where name in
        defaultScheduleStatusFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where name is not null
        defaultScheduleStatusFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where name contains
        defaultScheduleStatusFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where name does not contain
        defaultScheduleStatusFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where color equals to
        defaultScheduleStatusFiltering("color.equals=" + DEFAULT_COLOR, "color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where color in
        defaultScheduleStatusFiltering("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR, "color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where color is not null
        defaultScheduleStatusFiltering("color.specified=true", "color.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByColorContainsSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where color contains
        defaultScheduleStatusFiltering("color.contains=" + DEFAULT_COLOR, "color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where color does not contain
        defaultScheduleStatusFiltering("color.doesNotContain=" + UPDATED_COLOR, "color.doesNotContain=" + DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesBySortNoIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where sortNo equals to
        defaultScheduleStatusFiltering("sortNo.equals=" + DEFAULT_SORT_NO, "sortNo.equals=" + UPDATED_SORT_NO);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesBySortNoIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where sortNo in
        defaultScheduleStatusFiltering("sortNo.in=" + DEFAULT_SORT_NO + "," + UPDATED_SORT_NO, "sortNo.in=" + UPDATED_SORT_NO);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesBySortNoIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where sortNo is not null
        defaultScheduleStatusFiltering("sortNo.specified=true", "sortNo.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduleStatusesBySortNoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where sortNo is greater than or equal to
        defaultScheduleStatusFiltering("sortNo.greaterThanOrEqual=" + DEFAULT_SORT_NO, "sortNo.greaterThanOrEqual=" + UPDATED_SORT_NO);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesBySortNoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where sortNo is less than or equal to
        defaultScheduleStatusFiltering("sortNo.lessThanOrEqual=" + DEFAULT_SORT_NO, "sortNo.lessThanOrEqual=" + SMALLER_SORT_NO);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesBySortNoIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where sortNo is less than
        defaultScheduleStatusFiltering("sortNo.lessThan=" + UPDATED_SORT_NO, "sortNo.lessThan=" + DEFAULT_SORT_NO);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesBySortNoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where sortNo is greater than
        defaultScheduleStatusFiltering("sortNo.greaterThan=" + SMALLER_SORT_NO, "sortNo.greaterThan=" + DEFAULT_SORT_NO);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByIsDefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where isDefault equals to
        defaultScheduleStatusFiltering("isDefault.equals=" + DEFAULT_IS_DEFAULT, "isDefault.equals=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByIsDefaultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where isDefault in
        defaultScheduleStatusFiltering(
            "isDefault.in=" + DEFAULT_IS_DEFAULT + "," + UPDATED_IS_DEFAULT,
            "isDefault.in=" + UPDATED_IS_DEFAULT
        );
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByIsDefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where isDefault is not null
        defaultScheduleStatusFiltering("isDefault.specified=true", "isDefault.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByIsTerminalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where isTerminal equals to
        defaultScheduleStatusFiltering("isTerminal.equals=" + DEFAULT_IS_TERMINAL, "isTerminal.equals=" + UPDATED_IS_TERMINAL);
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByIsTerminalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where isTerminal in
        defaultScheduleStatusFiltering(
            "isTerminal.in=" + DEFAULT_IS_TERMINAL + "," + UPDATED_IS_TERMINAL,
            "isTerminal.in=" + UPDATED_IS_TERMINAL
        );
    }

    @Test
    @Transactional
    void getAllScheduleStatusesByIsTerminalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        // Get all the scheduleStatusList where isTerminal is not null
        defaultScheduleStatusFiltering("isTerminal.specified=true", "isTerminal.specified=false");
    }

    private void defaultScheduleStatusFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultScheduleStatusShouldBeFound(shouldBeFound);
        defaultScheduleStatusShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultScheduleStatusShouldBeFound(String filter) throws Exception {
        restScheduleStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduleStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].sortNo").value(hasItem(DEFAULT_SORT_NO)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)))
            .andExpect(jsonPath("$.[*].isTerminal").value(hasItem(DEFAULT_IS_TERMINAL)));

        // Check, that the count call also returns 1
        restScheduleStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultScheduleStatusShouldNotBeFound(String filter) throws Exception {
        restScheduleStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restScheduleStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingScheduleStatus() throws Exception {
        // Get the scheduleStatus
        restScheduleStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScheduleStatus() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduleStatus
        ScheduleStatus updatedScheduleStatus = scheduleStatusRepository.findById(scheduleStatus.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedScheduleStatus are not directly saved in db
        em.detach(updatedScheduleStatus);
        updatedScheduleStatus
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .color(UPDATED_COLOR)
            .sortNo(UPDATED_SORT_NO)
            .isDefault(UPDATED_IS_DEFAULT)
            .isTerminal(UPDATED_IS_TERMINAL);
        ScheduleStatusDTO scheduleStatusDTO = scheduleStatusMapper.toDto(updatedScheduleStatus);

        restScheduleStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduleStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduleStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScheduleStatusToMatchAllProperties(updatedScheduleStatus);
    }

    @Test
    @Transactional
    void putNonExistingScheduleStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduleStatus.setId(longCount.incrementAndGet());

        // Create the ScheduleStatus
        ScheduleStatusDTO scheduleStatusDTO = scheduleStatusMapper.toDto(scheduleStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduleStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduleStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScheduleStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduleStatus.setId(longCount.incrementAndGet());

        // Create the ScheduleStatus
        ScheduleStatusDTO scheduleStatusDTO = scheduleStatusMapper.toDto(scheduleStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduleStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScheduleStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduleStatus.setId(longCount.incrementAndGet());

        // Create the ScheduleStatus
        ScheduleStatusDTO scheduleStatusDTO = scheduleStatusMapper.toDto(scheduleStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduleStatusWithPatch() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduleStatus using partial update
        ScheduleStatus partialUpdatedScheduleStatus = new ScheduleStatus();
        partialUpdatedScheduleStatus.setId(scheduleStatus.getId());

        partialUpdatedScheduleStatus.name(UPDATED_NAME).color(UPDATED_COLOR).isDefault(UPDATED_IS_DEFAULT).isTerminal(UPDATED_IS_TERMINAL);

        restScheduleStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScheduleStatus))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScheduleStatusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedScheduleStatus, scheduleStatus),
            getPersistedScheduleStatus(scheduleStatus)
        );
    }

    @Test
    @Transactional
    void fullUpdateScheduleStatusWithPatch() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduleStatus using partial update
        ScheduleStatus partialUpdatedScheduleStatus = new ScheduleStatus();
        partialUpdatedScheduleStatus.setId(scheduleStatus.getId());

        partialUpdatedScheduleStatus
            .code(UPDATED_CODE)
            .name(UPDATED_NAME)
            .color(UPDATED_COLOR)
            .sortNo(UPDATED_SORT_NO)
            .isDefault(UPDATED_IS_DEFAULT)
            .isTerminal(UPDATED_IS_TERMINAL);

        restScheduleStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduleStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScheduleStatus))
            )
            .andExpect(status().isOk());

        // Validate the ScheduleStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScheduleStatusUpdatableFieldsEquals(partialUpdatedScheduleStatus, getPersistedScheduleStatus(partialUpdatedScheduleStatus));
    }

    @Test
    @Transactional
    void patchNonExistingScheduleStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduleStatus.setId(longCount.incrementAndGet());

        // Create the ScheduleStatus
        ScheduleStatusDTO scheduleStatusDTO = scheduleStatusMapper.toDto(scheduleStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheduleStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scheduleStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScheduleStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduleStatus.setId(longCount.incrementAndGet());

        // Create the ScheduleStatus
        ScheduleStatusDTO scheduleStatusDTO = scheduleStatusMapper.toDto(scheduleStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scheduleStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduleStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScheduleStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduleStatus.setId(longCount.incrementAndGet());

        // Create the ScheduleStatus
        ScheduleStatusDTO scheduleStatusDTO = scheduleStatusMapper.toDto(scheduleStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleStatusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(scheduleStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduleStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScheduleStatus() throws Exception {
        // Initialize the database
        insertedScheduleStatus = scheduleStatusRepository.saveAndFlush(scheduleStatus);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the scheduleStatus
        restScheduleStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, scheduleStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return scheduleStatusRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ScheduleStatus getPersistedScheduleStatus(ScheduleStatus scheduleStatus) {
        return scheduleStatusRepository.findById(scheduleStatus.getId()).orElseThrow();
    }

    protected void assertPersistedScheduleStatusToMatchAllProperties(ScheduleStatus expectedScheduleStatus) {
        assertScheduleStatusAllPropertiesEquals(expectedScheduleStatus, getPersistedScheduleStatus(expectedScheduleStatus));
    }

    protected void assertPersistedScheduleStatusToMatchUpdatableProperties(ScheduleStatus expectedScheduleStatus) {
        assertScheduleStatusAllUpdatablePropertiesEquals(expectedScheduleStatus, getPersistedScheduleStatus(expectedScheduleStatus));
    }
}
