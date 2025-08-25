package com.blueseaheart.demo.web.rest;

import static com.blueseaheart.demo.domain.ScheduleAsserts.*;
import static com.blueseaheart.demo.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blueseaheart.demo.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blueseaheart.demo.IntegrationTest;
import com.blueseaheart.demo.domain.Category;
import com.blueseaheart.demo.domain.Schedule;
import com.blueseaheart.demo.domain.ScheduleStatus;
import com.blueseaheart.demo.domain.Tag;
import com.blueseaheart.demo.domain.User;
import com.blueseaheart.demo.domain.enumeration.Priority;
import com.blueseaheart.demo.domain.enumeration.Visibility;
import com.blueseaheart.demo.repository.ScheduleRepository;
import com.blueseaheart.demo.repository.UserRepository;
import com.blueseaheart.demo.service.ScheduleService;
import com.blueseaheart.demo.service.dto.ScheduleDTO;
import com.blueseaheart.demo.service.mapper.ScheduleMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ScheduleResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ScheduleResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ALL_DAY = false;
    private static final Boolean UPDATED_ALL_DAY = true;

    private static final ZonedDateTime DEFAULT_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Priority DEFAULT_PRIORITY = Priority.LOW;
    private static final Priority UPDATED_PRIORITY = Priority.MEDIUM;

    private static final ZonedDateTime DEFAULT_COMPLETED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_COMPLETED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_COMPLETED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Visibility DEFAULT_VISIBILITY = Visibility.PRIVATE;
    private static final Visibility UPDATED_VISIBILITY = Visibility.TEAM;

    private static final String ENTITY_API_URL = "/api/schedules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ScheduleRepository scheduleRepositoryMock;

    @Autowired
    private ScheduleMapper scheduleMapper;

    @Mock
    private ScheduleService scheduleServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduleMockMvc;

    private Schedule schedule;

    private Schedule insertedSchedule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Schedule createEntity() {
        return new Schedule()
            .title(DEFAULT_TITLE)
            .description(DEFAULT_DESCRIPTION)
            .location(DEFAULT_LOCATION)
            .allDay(DEFAULT_ALL_DAY)
            .startTime(DEFAULT_START_TIME)
            .endTime(DEFAULT_END_TIME)
            .priority(DEFAULT_PRIORITY)
            .completedAt(DEFAULT_COMPLETED_AT)
            .visibility(DEFAULT_VISIBILITY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Schedule createUpdatedEntity() {
        return new Schedule()
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .allDay(UPDATED_ALL_DAY)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .priority(UPDATED_PRIORITY)
            .completedAt(UPDATED_COMPLETED_AT)
            .visibility(UPDATED_VISIBILITY);
    }

    @BeforeEach
    void initTest() {
        schedule = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSchedule != null) {
            scheduleRepository.delete(insertedSchedule);
            insertedSchedule = null;
        }
    }

    @Test
    @Transactional
    void createSchedule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);
        var returnedScheduleDTO = om.readValue(
            restScheduleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ScheduleDTO.class
        );

        // Validate the Schedule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSchedule = scheduleMapper.toEntity(returnedScheduleDTO);
        assertScheduleUpdatableFieldsEquals(returnedSchedule, getPersistedSchedule(returnedSchedule));

        insertedSchedule = returnedSchedule;
    }

    @Test
    @Transactional
    void createScheduleWithExistingId() throws Exception {
        // Create the Schedule with an existing ID
        schedule.setId(1L);
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        schedule.setTitle(null);

        // Create the Schedule, which fails.
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        restScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAllDayIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        schedule.setAllDay(null);

        // Create the Schedule, which fails.
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        restScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartTimeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        schedule.setStartTime(null);

        // Create the Schedule, which fails.
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        restScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        schedule.setPriority(null);

        // Create the Schedule, which fails.
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        restScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVisibilityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        schedule.setVisibility(null);

        // Create the Schedule, which fails.
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        restScheduleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSchedules() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList
        restScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].allDay").value(hasItem(DEFAULT_ALL_DAY)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(sameInstant(DEFAULT_COMPLETED_AT))))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchedulesWithEagerRelationshipsIsEnabled() throws Exception {
        when(scheduleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restScheduleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(scheduleServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllSchedulesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(scheduleServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restScheduleMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(scheduleRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getSchedule() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get the schedule
        restScheduleMockMvc
            .perform(get(ENTITY_API_URL_ID, schedule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(schedule.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.allDay").value(DEFAULT_ALL_DAY))
            .andExpect(jsonPath("$.startTime").value(sameInstant(DEFAULT_START_TIME)))
            .andExpect(jsonPath("$.endTime").value(sameInstant(DEFAULT_END_TIME)))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.completedAt").value(sameInstant(DEFAULT_COMPLETED_AT)))
            .andExpect(jsonPath("$.visibility").value(DEFAULT_VISIBILITY.toString()));
    }

    @Test
    @Transactional
    void getSchedulesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        Long id = schedule.getId();

        defaultScheduleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultScheduleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultScheduleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSchedulesByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where title equals to
        defaultScheduleFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSchedulesByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where title in
        defaultScheduleFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSchedulesByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where title is not null
        defaultScheduleFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedulesByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where title contains
        defaultScheduleFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllSchedulesByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where title does not contain
        defaultScheduleFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllSchedulesByLocationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where location equals to
        defaultScheduleFiltering("location.equals=" + DEFAULT_LOCATION, "location.equals=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSchedulesByLocationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where location in
        defaultScheduleFiltering("location.in=" + DEFAULT_LOCATION + "," + UPDATED_LOCATION, "location.in=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSchedulesByLocationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where location is not null
        defaultScheduleFiltering("location.specified=true", "location.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedulesByLocationContainsSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where location contains
        defaultScheduleFiltering("location.contains=" + DEFAULT_LOCATION, "location.contains=" + UPDATED_LOCATION);
    }

    @Test
    @Transactional
    void getAllSchedulesByLocationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where location does not contain
        defaultScheduleFiltering("location.doesNotContain=" + UPDATED_LOCATION, "location.doesNotContain=" + DEFAULT_LOCATION);
    }

    @Test
    @Transactional
    void getAllSchedulesByAllDayIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where allDay equals to
        defaultScheduleFiltering("allDay.equals=" + DEFAULT_ALL_DAY, "allDay.equals=" + UPDATED_ALL_DAY);
    }

    @Test
    @Transactional
    void getAllSchedulesByAllDayIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where allDay in
        defaultScheduleFiltering("allDay.in=" + DEFAULT_ALL_DAY + "," + UPDATED_ALL_DAY, "allDay.in=" + UPDATED_ALL_DAY);
    }

    @Test
    @Transactional
    void getAllSchedulesByAllDayIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where allDay is not null
        defaultScheduleFiltering("allDay.specified=true", "allDay.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedulesByStartTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where startTime equals to
        defaultScheduleFiltering("startTime.equals=" + DEFAULT_START_TIME, "startTime.equals=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllSchedulesByStartTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where startTime in
        defaultScheduleFiltering("startTime.in=" + DEFAULT_START_TIME + "," + UPDATED_START_TIME, "startTime.in=" + UPDATED_START_TIME);
    }

    @Test
    @Transactional
    void getAllSchedulesByStartTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where startTime is not null
        defaultScheduleFiltering("startTime.specified=true", "startTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedulesByStartTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where startTime is greater than or equal to
        defaultScheduleFiltering(
            "startTime.greaterThanOrEqual=" + DEFAULT_START_TIME,
            "startTime.greaterThanOrEqual=" + UPDATED_START_TIME
        );
    }

    @Test
    @Transactional
    void getAllSchedulesByStartTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where startTime is less than or equal to
        defaultScheduleFiltering("startTime.lessThanOrEqual=" + DEFAULT_START_TIME, "startTime.lessThanOrEqual=" + SMALLER_START_TIME);
    }

    @Test
    @Transactional
    void getAllSchedulesByStartTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where startTime is less than
        defaultScheduleFiltering("startTime.lessThan=" + UPDATED_START_TIME, "startTime.lessThan=" + DEFAULT_START_TIME);
    }

    @Test
    @Transactional
    void getAllSchedulesByStartTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where startTime is greater than
        defaultScheduleFiltering("startTime.greaterThan=" + SMALLER_START_TIME, "startTime.greaterThan=" + DEFAULT_START_TIME);
    }

    @Test
    @Transactional
    void getAllSchedulesByEndTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where endTime equals to
        defaultScheduleFiltering("endTime.equals=" + DEFAULT_END_TIME, "endTime.equals=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllSchedulesByEndTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where endTime in
        defaultScheduleFiltering("endTime.in=" + DEFAULT_END_TIME + "," + UPDATED_END_TIME, "endTime.in=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllSchedulesByEndTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where endTime is not null
        defaultScheduleFiltering("endTime.specified=true", "endTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedulesByEndTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where endTime is greater than or equal to
        defaultScheduleFiltering("endTime.greaterThanOrEqual=" + DEFAULT_END_TIME, "endTime.greaterThanOrEqual=" + UPDATED_END_TIME);
    }

    @Test
    @Transactional
    void getAllSchedulesByEndTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where endTime is less than or equal to
        defaultScheduleFiltering("endTime.lessThanOrEqual=" + DEFAULT_END_TIME, "endTime.lessThanOrEqual=" + SMALLER_END_TIME);
    }

    @Test
    @Transactional
    void getAllSchedulesByEndTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where endTime is less than
        defaultScheduleFiltering("endTime.lessThan=" + UPDATED_END_TIME, "endTime.lessThan=" + DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    void getAllSchedulesByEndTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where endTime is greater than
        defaultScheduleFiltering("endTime.greaterThan=" + SMALLER_END_TIME, "endTime.greaterThan=" + DEFAULT_END_TIME);
    }

    @Test
    @Transactional
    void getAllSchedulesByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where priority equals to
        defaultScheduleFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllSchedulesByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where priority in
        defaultScheduleFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllSchedulesByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where priority is not null
        defaultScheduleFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedulesByCompletedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where completedAt equals to
        defaultScheduleFiltering("completedAt.equals=" + DEFAULT_COMPLETED_AT, "completedAt.equals=" + UPDATED_COMPLETED_AT);
    }

    @Test
    @Transactional
    void getAllSchedulesByCompletedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where completedAt in
        defaultScheduleFiltering(
            "completedAt.in=" + DEFAULT_COMPLETED_AT + "," + UPDATED_COMPLETED_AT,
            "completedAt.in=" + UPDATED_COMPLETED_AT
        );
    }

    @Test
    @Transactional
    void getAllSchedulesByCompletedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where completedAt is not null
        defaultScheduleFiltering("completedAt.specified=true", "completedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedulesByCompletedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where completedAt is greater than or equal to
        defaultScheduleFiltering(
            "completedAt.greaterThanOrEqual=" + DEFAULT_COMPLETED_AT,
            "completedAt.greaterThanOrEqual=" + UPDATED_COMPLETED_AT
        );
    }

    @Test
    @Transactional
    void getAllSchedulesByCompletedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where completedAt is less than or equal to
        defaultScheduleFiltering(
            "completedAt.lessThanOrEqual=" + DEFAULT_COMPLETED_AT,
            "completedAt.lessThanOrEqual=" + SMALLER_COMPLETED_AT
        );
    }

    @Test
    @Transactional
    void getAllSchedulesByCompletedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where completedAt is less than
        defaultScheduleFiltering("completedAt.lessThan=" + UPDATED_COMPLETED_AT, "completedAt.lessThan=" + DEFAULT_COMPLETED_AT);
    }

    @Test
    @Transactional
    void getAllSchedulesByCompletedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where completedAt is greater than
        defaultScheduleFiltering("completedAt.greaterThan=" + SMALLER_COMPLETED_AT, "completedAt.greaterThan=" + DEFAULT_COMPLETED_AT);
    }

    @Test
    @Transactional
    void getAllSchedulesByVisibilityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where visibility equals to
        defaultScheduleFiltering("visibility.equals=" + DEFAULT_VISIBILITY, "visibility.equals=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    void getAllSchedulesByVisibilityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where visibility in
        defaultScheduleFiltering("visibility.in=" + DEFAULT_VISIBILITY + "," + UPDATED_VISIBILITY, "visibility.in=" + UPDATED_VISIBILITY);
    }

    @Test
    @Transactional
    void getAllSchedulesByVisibilityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        // Get all the scheduleList where visibility is not null
        defaultScheduleFiltering("visibility.specified=true", "visibility.specified=false");
    }

    @Test
    @Transactional
    void getAllSchedulesByOwnerIsEqualToSomething() throws Exception {
        User owner;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            scheduleRepository.saveAndFlush(schedule);
            owner = UserResourceIT.createEntity();
        } else {
            owner = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(owner);
        em.flush();
        schedule.setOwner(owner);
        scheduleRepository.saveAndFlush(schedule);
        Long ownerId = owner.getId();
        // Get all the scheduleList where owner equals to ownerId
        defaultScheduleShouldBeFound("ownerId.equals=" + ownerId);

        // Get all the scheduleList where owner equals to (ownerId + 1)
        defaultScheduleShouldNotBeFound("ownerId.equals=" + (ownerId + 1));
    }

    @Test
    @Transactional
    void getAllSchedulesByStatusIsEqualToSomething() throws Exception {
        ScheduleStatus status;
        if (TestUtil.findAll(em, ScheduleStatus.class).isEmpty()) {
            scheduleRepository.saveAndFlush(schedule);
            status = ScheduleStatusResourceIT.createEntity();
        } else {
            status = TestUtil.findAll(em, ScheduleStatus.class).get(0);
        }
        em.persist(status);
        em.flush();
        schedule.setStatus(status);
        scheduleRepository.saveAndFlush(schedule);
        Long statusId = status.getId();
        // Get all the scheduleList where status equals to statusId
        defaultScheduleShouldBeFound("statusId.equals=" + statusId);

        // Get all the scheduleList where status equals to (statusId + 1)
        defaultScheduleShouldNotBeFound("statusId.equals=" + (statusId + 1));
    }

    @Test
    @Transactional
    void getAllSchedulesByCategoryIsEqualToSomething() throws Exception {
        Category category;
        if (TestUtil.findAll(em, Category.class).isEmpty()) {
            scheduleRepository.saveAndFlush(schedule);
            category = CategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, Category.class).get(0);
        }
        em.persist(category);
        em.flush();
        schedule.setCategory(category);
        scheduleRepository.saveAndFlush(schedule);
        Long categoryId = category.getId();
        // Get all the scheduleList where category equals to categoryId
        defaultScheduleShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the scheduleList where category equals to (categoryId + 1)
        defaultScheduleShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllSchedulesByTagsIsEqualToSomething() throws Exception {
        Tag tags;
        if (TestUtil.findAll(em, Tag.class).isEmpty()) {
            scheduleRepository.saveAndFlush(schedule);
            tags = TagResourceIT.createEntity();
        } else {
            tags = TestUtil.findAll(em, Tag.class).get(0);
        }
        em.persist(tags);
        em.flush();
        schedule.addTags(tags);
        scheduleRepository.saveAndFlush(schedule);
        Long tagsId = tags.getId();
        // Get all the scheduleList where tags equals to tagsId
        defaultScheduleShouldBeFound("tagsId.equals=" + tagsId);

        // Get all the scheduleList where tags equals to (tagsId + 1)
        defaultScheduleShouldNotBeFound("tagsId.equals=" + (tagsId + 1));
    }

    private void defaultScheduleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultScheduleShouldBeFound(shouldBeFound);
        defaultScheduleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultScheduleShouldBeFound(String filter) throws Exception {
        restScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(schedule.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].allDay").value(hasItem(DEFAULT_ALL_DAY)))
            .andExpect(jsonPath("$.[*].startTime").value(hasItem(sameInstant(DEFAULT_START_TIME))))
            .andExpect(jsonPath("$.[*].endTime").value(hasItem(sameInstant(DEFAULT_END_TIME))))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(sameInstant(DEFAULT_COMPLETED_AT))))
            .andExpect(jsonPath("$.[*].visibility").value(hasItem(DEFAULT_VISIBILITY.toString())));

        // Check, that the count call also returns 1
        restScheduleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultScheduleShouldNotBeFound(String filter) throws Exception {
        restScheduleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restScheduleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSchedule() throws Exception {
        // Get the schedule
        restScheduleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSchedule() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the schedule
        Schedule updatedSchedule = scheduleRepository.findById(schedule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSchedule are not directly saved in db
        em.detach(updatedSchedule);
        updatedSchedule
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .allDay(UPDATED_ALL_DAY)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .priority(UPDATED_PRIORITY)
            .completedAt(UPDATED_COMPLETED_AT)
            .visibility(UPDATED_VISIBILITY);
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(updatedSchedule);

        restScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduleDTO))
            )
            .andExpect(status().isOk());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScheduleToMatchAllProperties(updatedSchedule);
    }

    @Test
    @Transactional
    void putNonExistingSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedule.setId(longCount.incrementAndGet());

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedule.setId(longCount.incrementAndGet());

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedule.setId(longCount.incrementAndGet());

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduleWithPatch() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the schedule using partial update
        Schedule partialUpdatedSchedule = new Schedule();
        partialUpdatedSchedule.setId(schedule.getId());

        partialUpdatedSchedule
            .title(UPDATED_TITLE)
            .priority(UPDATED_PRIORITY)
            .completedAt(UPDATED_COMPLETED_AT)
            .visibility(UPDATED_VISIBILITY);

        restScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSchedule))
            )
            .andExpect(status().isOk());

        // Validate the Schedule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScheduleUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedSchedule, schedule), getPersistedSchedule(schedule));
    }

    @Test
    @Transactional
    void fullUpdateScheduleWithPatch() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the schedule using partial update
        Schedule partialUpdatedSchedule = new Schedule();
        partialUpdatedSchedule.setId(schedule.getId());

        partialUpdatedSchedule
            .title(UPDATED_TITLE)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .allDay(UPDATED_ALL_DAY)
            .startTime(UPDATED_START_TIME)
            .endTime(UPDATED_END_TIME)
            .priority(UPDATED_PRIORITY)
            .completedAt(UPDATED_COMPLETED_AT)
            .visibility(UPDATED_VISIBILITY);

        restScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSchedule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSchedule))
            )
            .andExpect(status().isOk());

        // Validate the Schedule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScheduleUpdatableFieldsEquals(partialUpdatedSchedule, getPersistedSchedule(partialUpdatedSchedule));
    }

    @Test
    @Transactional
    void patchNonExistingSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedule.setId(longCount.incrementAndGet());

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheduleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedule.setId(longCount.incrementAndGet());

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scheduleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSchedule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        schedule.setId(longCount.incrementAndGet());

        // Create the Schedule
        ScheduleDTO scheduleDTO = scheduleMapper.toDto(schedule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(scheduleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Schedule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSchedule() throws Exception {
        // Initialize the database
        insertedSchedule = scheduleRepository.saveAndFlush(schedule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the schedule
        restScheduleMockMvc
            .perform(delete(ENTITY_API_URL_ID, schedule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return scheduleRepository.count();
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

    protected Schedule getPersistedSchedule(Schedule schedule) {
        return scheduleRepository.findById(schedule.getId()).orElseThrow();
    }

    protected void assertPersistedScheduleToMatchAllProperties(Schedule expectedSchedule) {
        assertScheduleAllPropertiesEquals(expectedSchedule, getPersistedSchedule(expectedSchedule));
    }

    protected void assertPersistedScheduleToMatchUpdatableProperties(Schedule expectedSchedule) {
        assertScheduleAllUpdatablePropertiesEquals(expectedSchedule, getPersistedSchedule(expectedSchedule));
    }
}
