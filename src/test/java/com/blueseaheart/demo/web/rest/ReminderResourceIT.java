package com.blueseaheart.demo.web.rest;

import static com.blueseaheart.demo.domain.ReminderAsserts.*;
import static com.blueseaheart.demo.web.rest.TestUtil.createUpdateProxyForBean;
import static com.blueseaheart.demo.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.blueseaheart.demo.IntegrationTest;
import com.blueseaheart.demo.domain.Reminder;
import com.blueseaheart.demo.domain.Schedule;
import com.blueseaheart.demo.domain.enumeration.ReminderChannel;
import com.blueseaheart.demo.repository.ReminderRepository;
import com.blueseaheart.demo.service.dto.ReminderDTO;
import com.blueseaheart.demo.service.mapper.ReminderMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
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
 * Integration tests for the {@link ReminderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReminderResourceIT {

    private static final ZonedDateTime DEFAULT_REMIND_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REMIND_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_REMIND_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final ReminderChannel DEFAULT_CHANNEL = ReminderChannel.IN_APP;
    private static final ReminderChannel UPDATED_CHANNEL = ReminderChannel.EMAIL;

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SENT = false;
    private static final Boolean UPDATED_SENT = true;

    private static final Integer DEFAULT_ATTEMPT_COUNT = 1;
    private static final Integer UPDATED_ATTEMPT_COUNT = 2;
    private static final Integer SMALLER_ATTEMPT_COUNT = 1 - 1;

    private static final ZonedDateTime DEFAULT_LAST_ATTEMPT_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_ATTEMPT_AT = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final ZonedDateTime SMALLER_LAST_ATTEMPT_AT = ZonedDateTime.ofInstant(Instant.ofEpochMilli(-1L), ZoneOffset.UTC);

    private static final String DEFAULT_LAST_ERROR_MSG = "AAAAAAAAAA";
    private static final String UPDATED_LAST_ERROR_MSG = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_MSG = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MSG = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reminders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ReminderMapper reminderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReminderMockMvc;

    private Reminder reminder;

    private Reminder insertedReminder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reminder createEntity() {
        return new Reminder()
            .remindAt(DEFAULT_REMIND_AT)
            .channel(DEFAULT_CHANNEL)
            .subject(DEFAULT_SUBJECT)
            .content(DEFAULT_CONTENT)
            .sent(DEFAULT_SENT)
            .attemptCount(DEFAULT_ATTEMPT_COUNT)
            .lastAttemptAt(DEFAULT_LAST_ATTEMPT_AT)
            .lastErrorMsg(DEFAULT_LAST_ERROR_MSG)
            .errorMsg(DEFAULT_ERROR_MSG);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Reminder createUpdatedEntity() {
        return new Reminder()
            .remindAt(UPDATED_REMIND_AT)
            .channel(UPDATED_CHANNEL)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .sent(UPDATED_SENT)
            .attemptCount(UPDATED_ATTEMPT_COUNT)
            .lastAttemptAt(UPDATED_LAST_ATTEMPT_AT)
            .lastErrorMsg(UPDATED_LAST_ERROR_MSG)
            .errorMsg(UPDATED_ERROR_MSG);
    }

    @BeforeEach
    void initTest() {
        reminder = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReminder != null) {
            reminderRepository.delete(insertedReminder);
            insertedReminder = null;
        }
    }

    @Test
    @Transactional
    void createReminder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);
        var returnedReminderDTO = om.readValue(
            restReminderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reminderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReminderDTO.class
        );

        // Validate the Reminder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReminder = reminderMapper.toEntity(returnedReminderDTO);
        assertReminderUpdatableFieldsEquals(returnedReminder, getPersistedReminder(returnedReminder));

        insertedReminder = returnedReminder;
    }

    @Test
    @Transactional
    void createReminderWithExistingId() throws Exception {
        // Create the Reminder with an existing ID
        reminder.setId(1L);
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReminderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reminderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRemindAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reminder.setRemindAt(null);

        // Create the Reminder, which fails.
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        restReminderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reminderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChannelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reminder.setChannel(null);

        // Create the Reminder, which fails.
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        restReminderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reminderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reminder.setSent(null);

        // Create the Reminder, which fails.
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        restReminderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reminderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReminders() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList
        restReminderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reminder.getId().intValue())))
            .andExpect(jsonPath("$.[*].remindAt").value(hasItem(sameInstant(DEFAULT_REMIND_AT))))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].sent").value(hasItem(DEFAULT_SENT)))
            .andExpect(jsonPath("$.[*].attemptCount").value(hasItem(DEFAULT_ATTEMPT_COUNT)))
            .andExpect(jsonPath("$.[*].lastAttemptAt").value(hasItem(sameInstant(DEFAULT_LAST_ATTEMPT_AT))))
            .andExpect(jsonPath("$.[*].lastErrorMsg").value(hasItem(DEFAULT_LAST_ERROR_MSG)))
            .andExpect(jsonPath("$.[*].errorMsg").value(hasItem(DEFAULT_ERROR_MSG)));
    }

    @Test
    @Transactional
    void getReminder() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get the reminder
        restReminderMockMvc
            .perform(get(ENTITY_API_URL_ID, reminder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reminder.getId().intValue()))
            .andExpect(jsonPath("$.remindAt").value(sameInstant(DEFAULT_REMIND_AT)))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL.toString()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.sent").value(DEFAULT_SENT))
            .andExpect(jsonPath("$.attemptCount").value(DEFAULT_ATTEMPT_COUNT))
            .andExpect(jsonPath("$.lastAttemptAt").value(sameInstant(DEFAULT_LAST_ATTEMPT_AT)))
            .andExpect(jsonPath("$.lastErrorMsg").value(DEFAULT_LAST_ERROR_MSG))
            .andExpect(jsonPath("$.errorMsg").value(DEFAULT_ERROR_MSG));
    }

    @Test
    @Transactional
    void getRemindersByIdFiltering() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        Long id = reminder.getId();

        defaultReminderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReminderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReminderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllRemindersByRemindAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where remindAt equals to
        defaultReminderFiltering("remindAt.equals=" + DEFAULT_REMIND_AT, "remindAt.equals=" + UPDATED_REMIND_AT);
    }

    @Test
    @Transactional
    void getAllRemindersByRemindAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where remindAt in
        defaultReminderFiltering("remindAt.in=" + DEFAULT_REMIND_AT + "," + UPDATED_REMIND_AT, "remindAt.in=" + UPDATED_REMIND_AT);
    }

    @Test
    @Transactional
    void getAllRemindersByRemindAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where remindAt is not null
        defaultReminderFiltering("remindAt.specified=true", "remindAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRemindersByRemindAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where remindAt is greater than or equal to
        defaultReminderFiltering("remindAt.greaterThanOrEqual=" + DEFAULT_REMIND_AT, "remindAt.greaterThanOrEqual=" + UPDATED_REMIND_AT);
    }

    @Test
    @Transactional
    void getAllRemindersByRemindAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where remindAt is less than or equal to
        defaultReminderFiltering("remindAt.lessThanOrEqual=" + DEFAULT_REMIND_AT, "remindAt.lessThanOrEqual=" + SMALLER_REMIND_AT);
    }

    @Test
    @Transactional
    void getAllRemindersByRemindAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where remindAt is less than
        defaultReminderFiltering("remindAt.lessThan=" + UPDATED_REMIND_AT, "remindAt.lessThan=" + DEFAULT_REMIND_AT);
    }

    @Test
    @Transactional
    void getAllRemindersByRemindAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where remindAt is greater than
        defaultReminderFiltering("remindAt.greaterThan=" + SMALLER_REMIND_AT, "remindAt.greaterThan=" + DEFAULT_REMIND_AT);
    }

    @Test
    @Transactional
    void getAllRemindersByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where channel equals to
        defaultReminderFiltering("channel.equals=" + DEFAULT_CHANNEL, "channel.equals=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllRemindersByChannelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where channel in
        defaultReminderFiltering("channel.in=" + DEFAULT_CHANNEL + "," + UPDATED_CHANNEL, "channel.in=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllRemindersByChannelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where channel is not null
        defaultReminderFiltering("channel.specified=true", "channel.specified=false");
    }

    @Test
    @Transactional
    void getAllRemindersBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where subject equals to
        defaultReminderFiltering("subject.equals=" + DEFAULT_SUBJECT, "subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllRemindersBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where subject in
        defaultReminderFiltering("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT, "subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllRemindersBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where subject is not null
        defaultReminderFiltering("subject.specified=true", "subject.specified=false");
    }

    @Test
    @Transactional
    void getAllRemindersBySubjectContainsSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where subject contains
        defaultReminderFiltering("subject.contains=" + DEFAULT_SUBJECT, "subject.contains=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllRemindersBySubjectNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where subject does not contain
        defaultReminderFiltering("subject.doesNotContain=" + UPDATED_SUBJECT, "subject.doesNotContain=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllRemindersBySentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where sent equals to
        defaultReminderFiltering("sent.equals=" + DEFAULT_SENT, "sent.equals=" + UPDATED_SENT);
    }

    @Test
    @Transactional
    void getAllRemindersBySentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where sent in
        defaultReminderFiltering("sent.in=" + DEFAULT_SENT + "," + UPDATED_SENT, "sent.in=" + UPDATED_SENT);
    }

    @Test
    @Transactional
    void getAllRemindersBySentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where sent is not null
        defaultReminderFiltering("sent.specified=true", "sent.specified=false");
    }

    @Test
    @Transactional
    void getAllRemindersByAttemptCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where attemptCount equals to
        defaultReminderFiltering("attemptCount.equals=" + DEFAULT_ATTEMPT_COUNT, "attemptCount.equals=" + UPDATED_ATTEMPT_COUNT);
    }

    @Test
    @Transactional
    void getAllRemindersByAttemptCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where attemptCount in
        defaultReminderFiltering(
            "attemptCount.in=" + DEFAULT_ATTEMPT_COUNT + "," + UPDATED_ATTEMPT_COUNT,
            "attemptCount.in=" + UPDATED_ATTEMPT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllRemindersByAttemptCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where attemptCount is not null
        defaultReminderFiltering("attemptCount.specified=true", "attemptCount.specified=false");
    }

    @Test
    @Transactional
    void getAllRemindersByAttemptCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where attemptCount is greater than or equal to
        defaultReminderFiltering(
            "attemptCount.greaterThanOrEqual=" + DEFAULT_ATTEMPT_COUNT,
            "attemptCount.greaterThanOrEqual=" + UPDATED_ATTEMPT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllRemindersByAttemptCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where attemptCount is less than or equal to
        defaultReminderFiltering(
            "attemptCount.lessThanOrEqual=" + DEFAULT_ATTEMPT_COUNT,
            "attemptCount.lessThanOrEqual=" + SMALLER_ATTEMPT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllRemindersByAttemptCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where attemptCount is less than
        defaultReminderFiltering("attemptCount.lessThan=" + UPDATED_ATTEMPT_COUNT, "attemptCount.lessThan=" + DEFAULT_ATTEMPT_COUNT);
    }

    @Test
    @Transactional
    void getAllRemindersByAttemptCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where attemptCount is greater than
        defaultReminderFiltering("attemptCount.greaterThan=" + SMALLER_ATTEMPT_COUNT, "attemptCount.greaterThan=" + DEFAULT_ATTEMPT_COUNT);
    }

    @Test
    @Transactional
    void getAllRemindersByLastAttemptAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where lastAttemptAt equals to
        defaultReminderFiltering("lastAttemptAt.equals=" + DEFAULT_LAST_ATTEMPT_AT, "lastAttemptAt.equals=" + UPDATED_LAST_ATTEMPT_AT);
    }

    @Test
    @Transactional
    void getAllRemindersByLastAttemptAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where lastAttemptAt in
        defaultReminderFiltering(
            "lastAttemptAt.in=" + DEFAULT_LAST_ATTEMPT_AT + "," + UPDATED_LAST_ATTEMPT_AT,
            "lastAttemptAt.in=" + UPDATED_LAST_ATTEMPT_AT
        );
    }

    @Test
    @Transactional
    void getAllRemindersByLastAttemptAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where lastAttemptAt is not null
        defaultReminderFiltering("lastAttemptAt.specified=true", "lastAttemptAt.specified=false");
    }

    @Test
    @Transactional
    void getAllRemindersByLastAttemptAtIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where lastAttemptAt is greater than or equal to
        defaultReminderFiltering(
            "lastAttemptAt.greaterThanOrEqual=" + DEFAULT_LAST_ATTEMPT_AT,
            "lastAttemptAt.greaterThanOrEqual=" + UPDATED_LAST_ATTEMPT_AT
        );
    }

    @Test
    @Transactional
    void getAllRemindersByLastAttemptAtIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where lastAttemptAt is less than or equal to
        defaultReminderFiltering(
            "lastAttemptAt.lessThanOrEqual=" + DEFAULT_LAST_ATTEMPT_AT,
            "lastAttemptAt.lessThanOrEqual=" + SMALLER_LAST_ATTEMPT_AT
        );
    }

    @Test
    @Transactional
    void getAllRemindersByLastAttemptAtIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where lastAttemptAt is less than
        defaultReminderFiltering("lastAttemptAt.lessThan=" + UPDATED_LAST_ATTEMPT_AT, "lastAttemptAt.lessThan=" + DEFAULT_LAST_ATTEMPT_AT);
    }

    @Test
    @Transactional
    void getAllRemindersByLastAttemptAtIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where lastAttemptAt is greater than
        defaultReminderFiltering(
            "lastAttemptAt.greaterThan=" + SMALLER_LAST_ATTEMPT_AT,
            "lastAttemptAt.greaterThan=" + DEFAULT_LAST_ATTEMPT_AT
        );
    }

    @Test
    @Transactional
    void getAllRemindersByLastErrorMsgIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where lastErrorMsg equals to
        defaultReminderFiltering("lastErrorMsg.equals=" + DEFAULT_LAST_ERROR_MSG, "lastErrorMsg.equals=" + UPDATED_LAST_ERROR_MSG);
    }

    @Test
    @Transactional
    void getAllRemindersByLastErrorMsgIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where lastErrorMsg in
        defaultReminderFiltering(
            "lastErrorMsg.in=" + DEFAULT_LAST_ERROR_MSG + "," + UPDATED_LAST_ERROR_MSG,
            "lastErrorMsg.in=" + UPDATED_LAST_ERROR_MSG
        );
    }

    @Test
    @Transactional
    void getAllRemindersByLastErrorMsgIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where lastErrorMsg is not null
        defaultReminderFiltering("lastErrorMsg.specified=true", "lastErrorMsg.specified=false");
    }

    @Test
    @Transactional
    void getAllRemindersByLastErrorMsgContainsSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where lastErrorMsg contains
        defaultReminderFiltering("lastErrorMsg.contains=" + DEFAULT_LAST_ERROR_MSG, "lastErrorMsg.contains=" + UPDATED_LAST_ERROR_MSG);
    }

    @Test
    @Transactional
    void getAllRemindersByLastErrorMsgNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where lastErrorMsg does not contain
        defaultReminderFiltering(
            "lastErrorMsg.doesNotContain=" + UPDATED_LAST_ERROR_MSG,
            "lastErrorMsg.doesNotContain=" + DEFAULT_LAST_ERROR_MSG
        );
    }

    @Test
    @Transactional
    void getAllRemindersByErrorMsgIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where errorMsg equals to
        defaultReminderFiltering("errorMsg.equals=" + DEFAULT_ERROR_MSG, "errorMsg.equals=" + UPDATED_ERROR_MSG);
    }

    @Test
    @Transactional
    void getAllRemindersByErrorMsgIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where errorMsg in
        defaultReminderFiltering("errorMsg.in=" + DEFAULT_ERROR_MSG + "," + UPDATED_ERROR_MSG, "errorMsg.in=" + UPDATED_ERROR_MSG);
    }

    @Test
    @Transactional
    void getAllRemindersByErrorMsgIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where errorMsg is not null
        defaultReminderFiltering("errorMsg.specified=true", "errorMsg.specified=false");
    }

    @Test
    @Transactional
    void getAllRemindersByErrorMsgContainsSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where errorMsg contains
        defaultReminderFiltering("errorMsg.contains=" + DEFAULT_ERROR_MSG, "errorMsg.contains=" + UPDATED_ERROR_MSG);
    }

    @Test
    @Transactional
    void getAllRemindersByErrorMsgNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        // Get all the reminderList where errorMsg does not contain
        defaultReminderFiltering("errorMsg.doesNotContain=" + UPDATED_ERROR_MSG, "errorMsg.doesNotContain=" + DEFAULT_ERROR_MSG);
    }

    @Test
    @Transactional
    void getAllRemindersByScheduleIsEqualToSomething() throws Exception {
        Schedule schedule;
        if (TestUtil.findAll(em, Schedule.class).isEmpty()) {
            reminderRepository.saveAndFlush(reminder);
            schedule = ScheduleResourceIT.createEntity();
        } else {
            schedule = TestUtil.findAll(em, Schedule.class).get(0);
        }
        em.persist(schedule);
        em.flush();
        reminder.setSchedule(schedule);
        reminderRepository.saveAndFlush(reminder);
        Long scheduleId = schedule.getId();
        // Get all the reminderList where schedule equals to scheduleId
        defaultReminderShouldBeFound("scheduleId.equals=" + scheduleId);

        // Get all the reminderList where schedule equals to (scheduleId + 1)
        defaultReminderShouldNotBeFound("scheduleId.equals=" + (scheduleId + 1));
    }

    private void defaultReminderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReminderShouldBeFound(shouldBeFound);
        defaultReminderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReminderShouldBeFound(String filter) throws Exception {
        restReminderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reminder.getId().intValue())))
            .andExpect(jsonPath("$.[*].remindAt").value(hasItem(sameInstant(DEFAULT_REMIND_AT))))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].sent").value(hasItem(DEFAULT_SENT)))
            .andExpect(jsonPath("$.[*].attemptCount").value(hasItem(DEFAULT_ATTEMPT_COUNT)))
            .andExpect(jsonPath("$.[*].lastAttemptAt").value(hasItem(sameInstant(DEFAULT_LAST_ATTEMPT_AT))))
            .andExpect(jsonPath("$.[*].lastErrorMsg").value(hasItem(DEFAULT_LAST_ERROR_MSG)))
            .andExpect(jsonPath("$.[*].errorMsg").value(hasItem(DEFAULT_ERROR_MSG)));

        // Check, that the count call also returns 1
        restReminderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReminderShouldNotBeFound(String filter) throws Exception {
        restReminderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReminderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReminder() throws Exception {
        // Get the reminder
        restReminderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReminder() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reminder
        Reminder updatedReminder = reminderRepository.findById(reminder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReminder are not directly saved in db
        em.detach(updatedReminder);
        updatedReminder
            .remindAt(UPDATED_REMIND_AT)
            .channel(UPDATED_CHANNEL)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .sent(UPDATED_SENT)
            .attemptCount(UPDATED_ATTEMPT_COUNT)
            .lastAttemptAt(UPDATED_LAST_ATTEMPT_AT)
            .lastErrorMsg(UPDATED_LAST_ERROR_MSG)
            .errorMsg(UPDATED_ERROR_MSG);
        ReminderDTO reminderDTO = reminderMapper.toDto(updatedReminder);

        restReminderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reminderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reminderDTO))
            )
            .andExpect(status().isOk());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReminderToMatchAllProperties(updatedReminder);
    }

    @Test
    @Transactional
    void putNonExistingReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reminder.setId(longCount.incrementAndGet());

        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReminderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reminderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reminder.setId(longCount.incrementAndGet());

        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReminderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reminder.setId(longCount.incrementAndGet());

        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReminderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reminderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReminderWithPatch() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reminder using partial update
        Reminder partialUpdatedReminder = new Reminder();
        partialUpdatedReminder.setId(reminder.getId());

        partialUpdatedReminder
            .remindAt(UPDATED_REMIND_AT)
            .channel(UPDATED_CHANNEL)
            .sent(UPDATED_SENT)
            .attemptCount(UPDATED_ATTEMPT_COUNT)
            .lastAttemptAt(UPDATED_LAST_ATTEMPT_AT)
            .lastErrorMsg(UPDATED_LAST_ERROR_MSG)
            .errorMsg(UPDATED_ERROR_MSG);

        restReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReminder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReminder))
            )
            .andExpect(status().isOk());

        // Validate the Reminder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReminderUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedReminder, reminder), getPersistedReminder(reminder));
    }

    @Test
    @Transactional
    void fullUpdateReminderWithPatch() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reminder using partial update
        Reminder partialUpdatedReminder = new Reminder();
        partialUpdatedReminder.setId(reminder.getId());

        partialUpdatedReminder
            .remindAt(UPDATED_REMIND_AT)
            .channel(UPDATED_CHANNEL)
            .subject(UPDATED_SUBJECT)
            .content(UPDATED_CONTENT)
            .sent(UPDATED_SENT)
            .attemptCount(UPDATED_ATTEMPT_COUNT)
            .lastAttemptAt(UPDATED_LAST_ATTEMPT_AT)
            .lastErrorMsg(UPDATED_LAST_ERROR_MSG)
            .errorMsg(UPDATED_ERROR_MSG);

        restReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReminder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReminder))
            )
            .andExpect(status().isOk());

        // Validate the Reminder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReminderUpdatableFieldsEquals(partialUpdatedReminder, getPersistedReminder(partialUpdatedReminder));
    }

    @Test
    @Transactional
    void patchNonExistingReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reminder.setId(longCount.incrementAndGet());

        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reminderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reminder.setId(longCount.incrementAndGet());

        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReminderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reminderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReminder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reminder.setId(longCount.incrementAndGet());

        // Create the Reminder
        ReminderDTO reminderDTO = reminderMapper.toDto(reminder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReminderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reminderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Reminder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReminder() throws Exception {
        // Initialize the database
        insertedReminder = reminderRepository.saveAndFlush(reminder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reminder
        restReminderMockMvc
            .perform(delete(ENTITY_API_URL_ID, reminder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reminderRepository.count();
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

    protected Reminder getPersistedReminder(Reminder reminder) {
        return reminderRepository.findById(reminder.getId()).orElseThrow();
    }

    protected void assertPersistedReminderToMatchAllProperties(Reminder expectedReminder) {
        assertReminderAllPropertiesEquals(expectedReminder, getPersistedReminder(expectedReminder));
    }

    protected void assertPersistedReminderToMatchUpdatableProperties(Reminder expectedReminder) {
        assertReminderAllUpdatablePropertiesEquals(expectedReminder, getPersistedReminder(expectedReminder));
    }
}
