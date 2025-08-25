package com.blueseaheart.demo.domain;

import static com.blueseaheart.demo.domain.CategoryTestSamples.*;
import static com.blueseaheart.demo.domain.ReminderTestSamples.*;
import static com.blueseaheart.demo.domain.ScheduleStatusTestSamples.*;
import static com.blueseaheart.demo.domain.ScheduleTestSamples.*;
import static com.blueseaheart.demo.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blueseaheart.demo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ScheduleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Schedule.class);
        Schedule schedule1 = getScheduleSample1();
        Schedule schedule2 = new Schedule();
        assertThat(schedule1).isNotEqualTo(schedule2);

        schedule2.setId(schedule1.getId());
        assertThat(schedule1).isEqualTo(schedule2);

        schedule2 = getScheduleSample2();
        assertThat(schedule1).isNotEqualTo(schedule2);
    }

    @Test
    void remindersTest() {
        Schedule schedule = getScheduleRandomSampleGenerator();
        Reminder reminderBack = getReminderRandomSampleGenerator();

        schedule.addReminders(reminderBack);
        assertThat(schedule.getReminders()).containsOnly(reminderBack);
        assertThat(reminderBack.getSchedule()).isEqualTo(schedule);

        schedule.removeReminders(reminderBack);
        assertThat(schedule.getReminders()).doesNotContain(reminderBack);
        assertThat(reminderBack.getSchedule()).isNull();

        schedule.reminders(new HashSet<>(Set.of(reminderBack)));
        assertThat(schedule.getReminders()).containsOnly(reminderBack);
        assertThat(reminderBack.getSchedule()).isEqualTo(schedule);

        schedule.setReminders(new HashSet<>());
        assertThat(schedule.getReminders()).doesNotContain(reminderBack);
        assertThat(reminderBack.getSchedule()).isNull();
    }

    @Test
    void statusTest() {
        Schedule schedule = getScheduleRandomSampleGenerator();
        ScheduleStatus scheduleStatusBack = getScheduleStatusRandomSampleGenerator();

        schedule.setStatus(scheduleStatusBack);
        assertThat(schedule.getStatus()).isEqualTo(scheduleStatusBack);

        schedule.status(null);
        assertThat(schedule.getStatus()).isNull();
    }

    @Test
    void categoryTest() {
        Schedule schedule = getScheduleRandomSampleGenerator();
        Category categoryBack = getCategoryRandomSampleGenerator();

        schedule.setCategory(categoryBack);
        assertThat(schedule.getCategory()).isEqualTo(categoryBack);

        schedule.category(null);
        assertThat(schedule.getCategory()).isNull();
    }

    @Test
    void tagsTest() {
        Schedule schedule = getScheduleRandomSampleGenerator();
        Tag tagBack = getTagRandomSampleGenerator();

        schedule.addTags(tagBack);
        assertThat(schedule.getTags()).containsOnly(tagBack);

        schedule.removeTags(tagBack);
        assertThat(schedule.getTags()).doesNotContain(tagBack);

        schedule.tags(new HashSet<>(Set.of(tagBack)));
        assertThat(schedule.getTags()).containsOnly(tagBack);

        schedule.setTags(new HashSet<>());
        assertThat(schedule.getTags()).doesNotContain(tagBack);
    }
}
