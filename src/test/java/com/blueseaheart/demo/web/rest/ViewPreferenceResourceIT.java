package com.blueseaheart.demo.web.rest;

import static com.blueseaheart.demo.domain.ViewPreferenceAsserts.*;
import static com.blueseaheart.demo.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blueseaheart.demo.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blueseaheart.demo.IntegrationTest;
import com.blueseaheart.demo.domain.User;
import com.blueseaheart.demo.domain.ViewPreference;
import com.blueseaheart.demo.domain.enumeration.ViewType;
import com.blueseaheart.demo.domain.enumeration.WeekStart;
import com.blueseaheart.demo.repository.UserRepository;
import com.blueseaheart.demo.repository.ViewPreferenceRepository;
import com.blueseaheart.demo.service.ViewPreferenceService;
import com.blueseaheart.demo.service.dto.ViewPreferenceDTO;
import com.blueseaheart.demo.service.mapper.ViewPreferenceMapper;
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
 * Integration tests for the {@link ViewPreferenceResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ViewPreferenceResourceIT {

    private static final ViewType DEFAULT_DEFAULT_VIEW = ViewType.DAY;
    private static final ViewType UPDATED_DEFAULT_VIEW = ViewType.WEEK;

    private static final ZonedDateTime DEFAULT_LAST_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_START = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_START = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ZonedDateTime DEFAULT_LAST_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_END = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_END = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final WeekStart DEFAULT_WEEK_START = WeekStart.MONDAY;
    private static final WeekStart UPDATED_WEEK_START = WeekStart.SUNDAY;

    private static final Boolean DEFAULT_SHOW_WEEKEND = false;
    private static final Boolean UPDATED_SHOW_WEEKEND = true;

    private static final String ENTITY_API_URL = "/api/view-preferences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ViewPreferenceRepository viewPreferenceRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private ViewPreferenceRepository viewPreferenceRepositoryMock;

    @Autowired
    private ViewPreferenceMapper viewPreferenceMapper;

    @Mock
    private ViewPreferenceService viewPreferenceServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restViewPreferenceMockMvc;

    private ViewPreference viewPreference;

    private ViewPreference insertedViewPreference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViewPreference createEntity() {
        return new ViewPreference()
            .defaultView(DEFAULT_DEFAULT_VIEW)
            .lastStart(DEFAULT_LAST_START)
            .lastEnd(DEFAULT_LAST_END)
            .weekStart(DEFAULT_WEEK_START)
            .showWeekend(DEFAULT_SHOW_WEEKEND);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ViewPreference createUpdatedEntity() {
        return new ViewPreference()
            .defaultView(UPDATED_DEFAULT_VIEW)
            .lastStart(UPDATED_LAST_START)
            .lastEnd(UPDATED_LAST_END)
            .weekStart(UPDATED_WEEK_START)
            .showWeekend(UPDATED_SHOW_WEEKEND);
    }

    @BeforeEach
    void initTest() {
        viewPreference = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedViewPreference != null) {
            viewPreferenceRepository.delete(insertedViewPreference);
            insertedViewPreference = null;
        }
    }

    @Test
    @Transactional
    void createViewPreference() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ViewPreference
        ViewPreferenceDTO viewPreferenceDTO = viewPreferenceMapper.toDto(viewPreference);
        var returnedViewPreferenceDTO = om.readValue(
            restViewPreferenceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewPreferenceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ViewPreferenceDTO.class
        );

        // Validate the ViewPreference in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedViewPreference = viewPreferenceMapper.toEntity(returnedViewPreferenceDTO);
        assertViewPreferenceUpdatableFieldsEquals(returnedViewPreference, getPersistedViewPreference(returnedViewPreference));

        insertedViewPreference = returnedViewPreference;
    }

    @Test
    @Transactional
    void createViewPreferenceWithExistingId() throws Exception {
        // Create the ViewPreference with an existing ID
        viewPreference.setId(1L);
        ViewPreferenceDTO viewPreferenceDTO = viewPreferenceMapper.toDto(viewPreference);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restViewPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewPreferenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ViewPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDefaultViewIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        viewPreference.setDefaultView(null);

        // Create the ViewPreference, which fails.
        ViewPreferenceDTO viewPreferenceDTO = viewPreferenceMapper.toDto(viewPreference);

        restViewPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewPreferenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWeekStartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        viewPreference.setWeekStart(null);

        // Create the ViewPreference, which fails.
        ViewPreferenceDTO viewPreferenceDTO = viewPreferenceMapper.toDto(viewPreference);

        restViewPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewPreferenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkShowWeekendIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        viewPreference.setShowWeekend(null);

        // Create the ViewPreference, which fails.
        ViewPreferenceDTO viewPreferenceDTO = viewPreferenceMapper.toDto(viewPreference);

        restViewPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewPreferenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllViewPreferences() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList
        restViewPreferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(viewPreference.getId().intValue())))
            .andExpect(jsonPath("$.[*].defaultView").value(hasItem(DEFAULT_DEFAULT_VIEW.toString())))
            .andExpect(jsonPath("$.[*].lastStart").value(hasItem(sameInstant(DEFAULT_LAST_START))))
            .andExpect(jsonPath("$.[*].lastEnd").value(hasItem(sameInstant(DEFAULT_LAST_END))))
            .andExpect(jsonPath("$.[*].weekStart").value(hasItem(DEFAULT_WEEK_START.toString())))
            .andExpect(jsonPath("$.[*].showWeekend").value(hasItem(DEFAULT_SHOW_WEEKEND)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllViewPreferencesWithEagerRelationshipsIsEnabled() throws Exception {
        when(viewPreferenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restViewPreferenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(viewPreferenceServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllViewPreferencesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(viewPreferenceServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restViewPreferenceMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(viewPreferenceRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getViewPreference() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get the viewPreference
        restViewPreferenceMockMvc
            .perform(get(ENTITY_API_URL_ID, viewPreference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(viewPreference.getId().intValue()))
            .andExpect(jsonPath("$.defaultView").value(DEFAULT_DEFAULT_VIEW.toString()))
            .andExpect(jsonPath("$.lastStart").value(sameInstant(DEFAULT_LAST_START)))
            .andExpect(jsonPath("$.lastEnd").value(sameInstant(DEFAULT_LAST_END)))
            .andExpect(jsonPath("$.weekStart").value(DEFAULT_WEEK_START.toString()))
            .andExpect(jsonPath("$.showWeekend").value(DEFAULT_SHOW_WEEKEND));
    }

    @Test
    @Transactional
    void getViewPreferencesByIdFiltering() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        Long id = viewPreference.getId();

        defaultViewPreferenceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultViewPreferenceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultViewPreferenceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllViewPreferencesByDefaultViewIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where defaultView equals to
        defaultViewPreferenceFiltering("defaultView.equals=" + DEFAULT_DEFAULT_VIEW, "defaultView.equals=" + UPDATED_DEFAULT_VIEW);
    }

    @Test
    @Transactional
    void getAllViewPreferencesByDefaultViewIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where defaultView in
        defaultViewPreferenceFiltering(
            "defaultView.in=" + DEFAULT_DEFAULT_VIEW + "," + UPDATED_DEFAULT_VIEW,
            "defaultView.in=" + UPDATED_DEFAULT_VIEW
        );
    }

    @Test
    @Transactional
    void getAllViewPreferencesByDefaultViewIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where defaultView is not null
        defaultViewPreferenceFiltering("defaultView.specified=true", "defaultView.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPreferencesByLastStartIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where lastStart equals to
        defaultViewPreferenceFiltering("lastStart.equals=" + DEFAULT_LAST_START, "lastStart.equals=" + UPDATED_LAST_START);
    }

    @Test
    @Transactional
    void getAllViewPreferencesByLastStartIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where lastStart in
        defaultViewPreferenceFiltering(
            "lastStart.in=" + DEFAULT_LAST_START + "," + UPDATED_LAST_START,
            "lastStart.in=" + UPDATED_LAST_START
        );
    }

    @Test
    @Transactional
    void getAllViewPreferencesByLastStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where lastStart is not null
        defaultViewPreferenceFiltering("lastStart.specified=true", "lastStart.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPreferencesByLastStartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where lastStart is greater than or equal to
        defaultViewPreferenceFiltering(
            "lastStart.greaterThanOrEqual=" + DEFAULT_LAST_START,
            "lastStart.greaterThanOrEqual=" + UPDATED_LAST_START
        );
    }

    @Test
    @Transactional
    void getAllViewPreferencesByLastStartIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where lastStart is less than or equal to
        defaultViewPreferenceFiltering(
            "lastStart.lessThanOrEqual=" + DEFAULT_LAST_START,
            "lastStart.lessThanOrEqual=" + SMALLER_LAST_START
        );
    }

    @Test
    @Transactional
    void getAllViewPreferencesByLastStartIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where lastStart is less than
        defaultViewPreferenceFiltering("lastStart.lessThan=" + UPDATED_LAST_START, "lastStart.lessThan=" + DEFAULT_LAST_START);
    }

    @Test
    @Transactional
    void getAllViewPreferencesByLastStartIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where lastStart is greater than
        defaultViewPreferenceFiltering("lastStart.greaterThan=" + SMALLER_LAST_START, "lastStart.greaterThan=" + DEFAULT_LAST_START);
    }

    @Test
    @Transactional
    void getAllViewPreferencesByLastEndIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where lastEnd equals to
        defaultViewPreferenceFiltering("lastEnd.equals=" + DEFAULT_LAST_END, "lastEnd.equals=" + UPDATED_LAST_END);
    }

    @Test
    @Transactional
    void getAllViewPreferencesByLastEndIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where lastEnd in
        defaultViewPreferenceFiltering("lastEnd.in=" + DEFAULT_LAST_END + "," + UPDATED_LAST_END, "lastEnd.in=" + UPDATED_LAST_END);
    }

    @Test
    @Transactional
    void getAllViewPreferencesByLastEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where lastEnd is not null
        defaultViewPreferenceFiltering("lastEnd.specified=true", "lastEnd.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPreferencesByLastEndIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where lastEnd is greater than or equal to
        defaultViewPreferenceFiltering("lastEnd.greaterThanOrEqual=" + DEFAULT_LAST_END, "lastEnd.greaterThanOrEqual=" + UPDATED_LAST_END);
    }

    @Test
    @Transactional
    void getAllViewPreferencesByLastEndIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where lastEnd is less than or equal to
        defaultViewPreferenceFiltering("lastEnd.lessThanOrEqual=" + DEFAULT_LAST_END, "lastEnd.lessThanOrEqual=" + SMALLER_LAST_END);
    }

    @Test
    @Transactional
    void getAllViewPreferencesByLastEndIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where lastEnd is less than
        defaultViewPreferenceFiltering("lastEnd.lessThan=" + UPDATED_LAST_END, "lastEnd.lessThan=" + DEFAULT_LAST_END);
    }

    @Test
    @Transactional
    void getAllViewPreferencesByLastEndIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where lastEnd is greater than
        defaultViewPreferenceFiltering("lastEnd.greaterThan=" + SMALLER_LAST_END, "lastEnd.greaterThan=" + DEFAULT_LAST_END);
    }

    @Test
    @Transactional
    void getAllViewPreferencesByWeekStartIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where weekStart equals to
        defaultViewPreferenceFiltering("weekStart.equals=" + DEFAULT_WEEK_START, "weekStart.equals=" + UPDATED_WEEK_START);
    }

    @Test
    @Transactional
    void getAllViewPreferencesByWeekStartIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where weekStart in
        defaultViewPreferenceFiltering(
            "weekStart.in=" + DEFAULT_WEEK_START + "," + UPDATED_WEEK_START,
            "weekStart.in=" + UPDATED_WEEK_START
        );
    }

    @Test
    @Transactional
    void getAllViewPreferencesByWeekStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where weekStart is not null
        defaultViewPreferenceFiltering("weekStart.specified=true", "weekStart.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPreferencesByShowWeekendIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where showWeekend equals to
        defaultViewPreferenceFiltering("showWeekend.equals=" + DEFAULT_SHOW_WEEKEND, "showWeekend.equals=" + UPDATED_SHOW_WEEKEND);
    }

    @Test
    @Transactional
    void getAllViewPreferencesByShowWeekendIsInShouldWork() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where showWeekend in
        defaultViewPreferenceFiltering(
            "showWeekend.in=" + DEFAULT_SHOW_WEEKEND + "," + UPDATED_SHOW_WEEKEND,
            "showWeekend.in=" + UPDATED_SHOW_WEEKEND
        );
    }

    @Test
    @Transactional
    void getAllViewPreferencesByShowWeekendIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        // Get all the viewPreferenceList where showWeekend is not null
        defaultViewPreferenceFiltering("showWeekend.specified=true", "showWeekend.specified=false");
    }

    @Test
    @Transactional
    void getAllViewPreferencesByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            viewPreferenceRepository.saveAndFlush(viewPreference);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        viewPreference.setUser(user);
        viewPreferenceRepository.saveAndFlush(viewPreference);
        Long userId = user.getId();
        // Get all the viewPreferenceList where user equals to userId
        defaultViewPreferenceShouldBeFound("userId.equals=" + userId);

        // Get all the viewPreferenceList where user equals to (userId + 1)
        defaultViewPreferenceShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultViewPreferenceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultViewPreferenceShouldBeFound(shouldBeFound);
        defaultViewPreferenceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultViewPreferenceShouldBeFound(String filter) throws Exception {
        restViewPreferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(viewPreference.getId().intValue())))
            .andExpect(jsonPath("$.[*].defaultView").value(hasItem(DEFAULT_DEFAULT_VIEW.toString())))
            .andExpect(jsonPath("$.[*].lastStart").value(hasItem(sameInstant(DEFAULT_LAST_START))))
            .andExpect(jsonPath("$.[*].lastEnd").value(hasItem(sameInstant(DEFAULT_LAST_END))))
            .andExpect(jsonPath("$.[*].weekStart").value(hasItem(DEFAULT_WEEK_START.toString())))
            .andExpect(jsonPath("$.[*].showWeekend").value(hasItem(DEFAULT_SHOW_WEEKEND)));

        // Check, that the count call also returns 1
        restViewPreferenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultViewPreferenceShouldNotBeFound(String filter) throws Exception {
        restViewPreferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restViewPreferenceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingViewPreference() throws Exception {
        // Get the viewPreference
        restViewPreferenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingViewPreference() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the viewPreference
        ViewPreference updatedViewPreference = viewPreferenceRepository.findById(viewPreference.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedViewPreference are not directly saved in db
        em.detach(updatedViewPreference);
        updatedViewPreference
            .defaultView(UPDATED_DEFAULT_VIEW)
            .lastStart(UPDATED_LAST_START)
            .lastEnd(UPDATED_LAST_END)
            .weekStart(UPDATED_WEEK_START)
            .showWeekend(UPDATED_SHOW_WEEKEND);
        ViewPreferenceDTO viewPreferenceDTO = viewPreferenceMapper.toDto(updatedViewPreference);

        restViewPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, viewPreferenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(viewPreferenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the ViewPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedViewPreferenceToMatchAllProperties(updatedViewPreference);
    }

    @Test
    @Transactional
    void putNonExistingViewPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewPreference.setId(longCount.incrementAndGet());

        // Create the ViewPreference
        ViewPreferenceDTO viewPreferenceDTO = viewPreferenceMapper.toDto(viewPreference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, viewPreferenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(viewPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchViewPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewPreference.setId(longCount.incrementAndGet());

        // Create the ViewPreference
        ViewPreferenceDTO viewPreferenceDTO = viewPreferenceMapper.toDto(viewPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(viewPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamViewPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewPreference.setId(longCount.incrementAndGet());

        // Create the ViewPreference
        ViewPreferenceDTO viewPreferenceDTO = viewPreferenceMapper.toDto(viewPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPreferenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(viewPreferenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ViewPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateViewPreferenceWithPatch() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the viewPreference using partial update
        ViewPreference partialUpdatedViewPreference = new ViewPreference();
        partialUpdatedViewPreference.setId(viewPreference.getId());

        partialUpdatedViewPreference.lastStart(UPDATED_LAST_START).weekStart(UPDATED_WEEK_START).showWeekend(UPDATED_SHOW_WEEKEND);

        restViewPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViewPreference.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedViewPreference))
            )
            .andExpect(status().isOk());

        // Validate the ViewPreference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertViewPreferenceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedViewPreference, viewPreference),
            getPersistedViewPreference(viewPreference)
        );
    }

    @Test
    @Transactional
    void fullUpdateViewPreferenceWithPatch() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the viewPreference using partial update
        ViewPreference partialUpdatedViewPreference = new ViewPreference();
        partialUpdatedViewPreference.setId(viewPreference.getId());

        partialUpdatedViewPreference
            .defaultView(UPDATED_DEFAULT_VIEW)
            .lastStart(UPDATED_LAST_START)
            .lastEnd(UPDATED_LAST_END)
            .weekStart(UPDATED_WEEK_START)
            .showWeekend(UPDATED_SHOW_WEEKEND);

        restViewPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedViewPreference.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedViewPreference))
            )
            .andExpect(status().isOk());

        // Validate the ViewPreference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertViewPreferenceUpdatableFieldsEquals(partialUpdatedViewPreference, getPersistedViewPreference(partialUpdatedViewPreference));
    }

    @Test
    @Transactional
    void patchNonExistingViewPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewPreference.setId(longCount.incrementAndGet());

        // Create the ViewPreference
        ViewPreferenceDTO viewPreferenceDTO = viewPreferenceMapper.toDto(viewPreference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restViewPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, viewPreferenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(viewPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchViewPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewPreference.setId(longCount.incrementAndGet());

        // Create the ViewPreference
        ViewPreferenceDTO viewPreferenceDTO = viewPreferenceMapper.toDto(viewPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(viewPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ViewPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamViewPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        viewPreference.setId(longCount.incrementAndGet());

        // Create the ViewPreference
        ViewPreferenceDTO viewPreferenceDTO = viewPreferenceMapper.toDto(viewPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restViewPreferenceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(viewPreferenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ViewPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteViewPreference() throws Exception {
        // Initialize the database
        insertedViewPreference = viewPreferenceRepository.saveAndFlush(viewPreference);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the viewPreference
        restViewPreferenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, viewPreference.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return viewPreferenceRepository.count();
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

    protected ViewPreference getPersistedViewPreference(ViewPreference viewPreference) {
        return viewPreferenceRepository.findById(viewPreference.getId()).orElseThrow();
    }

    protected void assertPersistedViewPreferenceToMatchAllProperties(ViewPreference expectedViewPreference) {
        assertViewPreferenceAllPropertiesEquals(expectedViewPreference, getPersistedViewPreference(expectedViewPreference));
    }

    protected void assertPersistedViewPreferenceToMatchUpdatableProperties(ViewPreference expectedViewPreference) {
        assertViewPreferenceAllUpdatablePropertiesEquals(expectedViewPreference, getPersistedViewPreference(expectedViewPreference));
    }
}
