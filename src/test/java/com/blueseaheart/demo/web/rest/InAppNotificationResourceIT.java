package com.blueseaheart.demo.web.rest;

import static com.blueseaheart.demo.domain.InAppNotificationAsserts.*;
import static com.blueseaheart.demo.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blueseaheart.demo.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blueseaheart.demo.IntegrationTest;
import com.blueseaheart.demo.domain.InAppNotification;
import com.blueseaheart.demo.domain.User;
import com.blueseaheart.demo.repository.InAppNotificationRepository;
import com.blueseaheart.demo.repository.UserRepository;
import com.blueseaheart.demo.service.InAppNotificationService;
import com.blueseaheart.demo.service.dto.InAppNotificationDTO;
import com.blueseaheart.demo.service.mapper.InAppNotificationMapper;
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
 * Integration tests for the {@link InAppNotificationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InAppNotificationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_CREATED_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final Boolean DEFAULT_READ = false;
    private static final Boolean UPDATED_READ = true;

    private static final String ENTITY_API_URL = "/api/in-app-notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InAppNotificationRepository inAppNotificationRepository;

    @Autowired
    private UserRepository userRepository;

    @Mock
    private InAppNotificationRepository inAppNotificationRepositoryMock;

    @Autowired
    private InAppNotificationMapper inAppNotificationMapper;

    @Mock
    private InAppNotificationService inAppNotificationServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInAppNotificationMockMvc;

    private InAppNotification inAppNotification;

    private InAppNotification insertedInAppNotification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InAppNotification createEntity() {
        return new InAppNotification().title(DEFAULT_TITLE).content(DEFAULT_CONTENT).createdAt(DEFAULT_CREATED_AT).read(DEFAULT_READ);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InAppNotification createUpdatedEntity() {
        return new InAppNotification().title(UPDATED_TITLE).content(UPDATED_CONTENT).createdAt(UPDATED_CREATED_AT).read(UPDATED_READ);
    }

    @BeforeEach
    void initTest() {
        inAppNotification = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedInAppNotification != null) {
            inAppNotificationRepository.delete(insertedInAppNotification);
            insertedInAppNotification = null;
        }
    }

    @Test
    @Transactional
    void createInAppNotification() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InAppNotification
        InAppNotificationDTO inAppNotificationDTO = inAppNotificationMapper.toDto(inAppNotification);
        var returnedInAppNotificationDTO = om.readValue(
            restInAppNotificationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inAppNotificationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InAppNotificationDTO.class
        );

        // Validate the InAppNotification in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedInAppNotification = inAppNotificationMapper.toEntity(returnedInAppNotificationDTO);
        assertInAppNotificationUpdatableFieldsEquals(returnedInAppNotification, getPersistedInAppNotification(returnedInAppNotification));

        insertedInAppNotification = returnedInAppNotification;
    }

    @Test
    @Transactional
    void createInAppNotificationWithExistingId() throws Exception {
        // Create the InAppNotification with an existing ID
        inAppNotification.setId(1L);
        InAppNotificationDTO inAppNotificationDTO = inAppNotificationMapper.toDto(inAppNotification);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInAppNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inAppNotificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the InAppNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inAppNotification.setTitle(null);

        // Create the InAppNotification, which fails.
        InAppNotificationDTO inAppNotificationDTO = inAppNotificationMapper.toDto(inAppNotification);

        restInAppNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inAppNotificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inAppNotification.setCreatedAt(null);

        // Create the InAppNotification, which fails.
        InAppNotificationDTO inAppNotificationDTO = inAppNotificationMapper.toDto(inAppNotification);

        restInAppNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inAppNotificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        inAppNotification.setRead(null);

        // Create the InAppNotification, which fails.
        InAppNotificationDTO inAppNotificationDTO = inAppNotificationMapper.toDto(inAppNotification);

        restInAppNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inAppNotificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInAppNotifications() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList
        restInAppNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inAppNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].read").value(hasItem(DEFAULT_READ)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInAppNotificationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(inAppNotificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInAppNotificationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(inAppNotificationServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInAppNotificationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(inAppNotificationServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInAppNotificationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(inAppNotificationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInAppNotification() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get the inAppNotification
        restInAppNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, inAppNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(inAppNotification.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.createdAt").value(sameInstant(DEFAULT_CREATED_AT)))
            .andExpect(jsonPath("$.read").value(DEFAULT_READ));
    }

    @Test
    @Transactional
    void getInAppNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        Long id = inAppNotification.getId();

        defaultInAppNotificationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultInAppNotificationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultInAppNotificationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where title equals to
        defaultInAppNotificationFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where title in
        defaultInAppNotificationFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where title is not null
        defaultInAppNotificationFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where title contains
        defaultInAppNotificationFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where title does not contain
        defaultInAppNotificationFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where createdAt equals to
        defaultInAppNotificationFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where createdAt in
        defaultInAppNotificationFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where createdAt is not null
        defaultInAppNotificationFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByCreatedAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where createdAt is greater than or equal to
        defaultInAppNotificationFiltering(
            "createdAt.greaterThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.greaterThanOrEqual=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByCreatedAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where createdAt is less than or equal to
        defaultInAppNotificationFiltering(
            "createdAt.lessThanOrEqual=" + DEFAULT_CREATED_AT,
            "createdAt.lessThanOrEqual=" + SMALLER_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByCreatedAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where createdAt is less than
        defaultInAppNotificationFiltering("createdAt.lessThan=" + UPDATED_CREATED_AT, "createdAt.lessThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByCreatedAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where createdAt is greater than
        defaultInAppNotificationFiltering("createdAt.greaterThan=" + SMALLER_CREATED_AT, "createdAt.greaterThan=" + DEFAULT_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByReadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where read equals to
        defaultInAppNotificationFiltering("read.equals=" + DEFAULT_READ, "read.equals=" + UPDATED_READ);
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByReadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where read in
        defaultInAppNotificationFiltering("read.in=" + DEFAULT_READ + "," + UPDATED_READ, "read.in=" + UPDATED_READ);
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByReadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        // Get all the inAppNotificationList where read is not null
        defaultInAppNotificationFiltering("read.specified=true", "read.specified=false");
    }

    @Test
    @Transactional
    void getAllInAppNotificationsByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            inAppNotificationRepository.saveAndFlush(inAppNotification);
            user = UserResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        inAppNotification.setUser(user);
        inAppNotificationRepository.saveAndFlush(inAppNotification);
        Long userId = user.getId();
        // Get all the inAppNotificationList where user equals to userId
        defaultInAppNotificationShouldBeFound("userId.equals=" + userId);

        // Get all the inAppNotificationList where user equals to (userId + 1)
        defaultInAppNotificationShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    private void defaultInAppNotificationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultInAppNotificationShouldBeFound(shouldBeFound);
        defaultInAppNotificationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultInAppNotificationShouldBeFound(String filter) throws Exception {
        restInAppNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(inAppNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(sameInstant(DEFAULT_CREATED_AT))))
            .andExpect(jsonPath("$.[*].read").value(hasItem(DEFAULT_READ)));

        // Check, that the count call also returns 1
        restInAppNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultInAppNotificationShouldNotBeFound(String filter) throws Exception {
        restInAppNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restInAppNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingInAppNotification() throws Exception {
        // Get the inAppNotification
        restInAppNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInAppNotification() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inAppNotification
        InAppNotification updatedInAppNotification = inAppNotificationRepository.findById(inAppNotification.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInAppNotification are not directly saved in db
        em.detach(updatedInAppNotification);
        updatedInAppNotification.title(UPDATED_TITLE).content(UPDATED_CONTENT).createdAt(UPDATED_CREATED_AT).read(UPDATED_READ);
        InAppNotificationDTO inAppNotificationDTO = inAppNotificationMapper.toDto(updatedInAppNotification);

        restInAppNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inAppNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inAppNotificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the InAppNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInAppNotificationToMatchAllProperties(updatedInAppNotification);
    }

    @Test
    @Transactional
    void putNonExistingInAppNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inAppNotification.setId(longCount.incrementAndGet());

        // Create the InAppNotification
        InAppNotificationDTO inAppNotificationDTO = inAppNotificationMapper.toDto(inAppNotification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInAppNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, inAppNotificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inAppNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InAppNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInAppNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inAppNotification.setId(longCount.incrementAndGet());

        // Create the InAppNotification
        InAppNotificationDTO inAppNotificationDTO = inAppNotificationMapper.toDto(inAppNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInAppNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(inAppNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InAppNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInAppNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inAppNotification.setId(longCount.incrementAndGet());

        // Create the InAppNotification
        InAppNotificationDTO inAppNotificationDTO = inAppNotificationMapper.toDto(inAppNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInAppNotificationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(inAppNotificationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InAppNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInAppNotificationWithPatch() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inAppNotification using partial update
        InAppNotification partialUpdatedInAppNotification = new InAppNotification();
        partialUpdatedInAppNotification.setId(inAppNotification.getId());

        partialUpdatedInAppNotification.content(UPDATED_CONTENT).createdAt(UPDATED_CREATED_AT).read(UPDATED_READ);

        restInAppNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInAppNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInAppNotification))
            )
            .andExpect(status().isOk());

        // Validate the InAppNotification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInAppNotificationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInAppNotification, inAppNotification),
            getPersistedInAppNotification(inAppNotification)
        );
    }

    @Test
    @Transactional
    void fullUpdateInAppNotificationWithPatch() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the inAppNotification using partial update
        InAppNotification partialUpdatedInAppNotification = new InAppNotification();
        partialUpdatedInAppNotification.setId(inAppNotification.getId());

        partialUpdatedInAppNotification.title(UPDATED_TITLE).content(UPDATED_CONTENT).createdAt(UPDATED_CREATED_AT).read(UPDATED_READ);

        restInAppNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInAppNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInAppNotification))
            )
            .andExpect(status().isOk());

        // Validate the InAppNotification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInAppNotificationUpdatableFieldsEquals(
            partialUpdatedInAppNotification,
            getPersistedInAppNotification(partialUpdatedInAppNotification)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInAppNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inAppNotification.setId(longCount.incrementAndGet());

        // Create the InAppNotification
        InAppNotificationDTO inAppNotificationDTO = inAppNotificationMapper.toDto(inAppNotification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInAppNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, inAppNotificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inAppNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InAppNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInAppNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inAppNotification.setId(longCount.incrementAndGet());

        // Create the InAppNotification
        InAppNotificationDTO inAppNotificationDTO = inAppNotificationMapper.toDto(inAppNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInAppNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(inAppNotificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the InAppNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInAppNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        inAppNotification.setId(longCount.incrementAndGet());

        // Create the InAppNotification
        InAppNotificationDTO inAppNotificationDTO = inAppNotificationMapper.toDto(inAppNotification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInAppNotificationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(inAppNotificationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InAppNotification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInAppNotification() throws Exception {
        // Initialize the database
        insertedInAppNotification = inAppNotificationRepository.saveAndFlush(inAppNotification);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the inAppNotification
        restInAppNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, inAppNotification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return inAppNotificationRepository.count();
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

    protected InAppNotification getPersistedInAppNotification(InAppNotification inAppNotification) {
        return inAppNotificationRepository.findById(inAppNotification.getId()).orElseThrow();
    }

    protected void assertPersistedInAppNotificationToMatchAllProperties(InAppNotification expectedInAppNotification) {
        assertInAppNotificationAllPropertiesEquals(expectedInAppNotification, getPersistedInAppNotification(expectedInAppNotification));
    }

    protected void assertPersistedInAppNotificationToMatchUpdatableProperties(InAppNotification expectedInAppNotification) {
        assertInAppNotificationAllUpdatablePropertiesEquals(
            expectedInAppNotification,
            getPersistedInAppNotification(expectedInAppNotification)
        );
    }
}
