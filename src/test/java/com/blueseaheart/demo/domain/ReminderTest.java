package com.blueseaheart.demo.domain;

import static com.blueseaheart.demo.domain.ReminderTestSamples.*;
import static com.blueseaheart.demo.domain.ScheduleTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blueseaheart.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ReminderTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Reminder.class);
        Reminder reminder1 = getReminderSample1();
        Reminder reminder2 = new Reminder();
        assertThat(reminder1).isNotEqualTo(reminder2);

        reminder2.setId(reminder1.getId());
        assertThat(reminder1).isEqualTo(reminder2);

        reminder2 = getReminderSample2();
        assertThat(reminder1).isNotEqualTo(reminder2);
    }

    @Test
    void scheduleTest() {
        Reminder reminder = getReminderRandomSampleGenerator();
        Schedule scheduleBack = getScheduleRandomSampleGenerator();

        reminder.setSchedule(scheduleBack);
        assertThat(reminder.getSchedule()).isEqualTo(scheduleBack);

        reminder.schedule(null);
        assertThat(reminder.getSchedule()).isNull();
    }
}
