package com.blueseaheart.demo.domain;

import static com.blueseaheart.demo.domain.ScheduleStatusTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blueseaheart.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScheduleStatusTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduleStatus.class);
        ScheduleStatus scheduleStatus1 = getScheduleStatusSample1();
        ScheduleStatus scheduleStatus2 = new ScheduleStatus();
        assertThat(scheduleStatus1).isNotEqualTo(scheduleStatus2);

        scheduleStatus2.setId(scheduleStatus1.getId());
        assertThat(scheduleStatus1).isEqualTo(scheduleStatus2);

        scheduleStatus2 = getScheduleStatusSample2();
        assertThat(scheduleStatus1).isNotEqualTo(scheduleStatus2);
    }
}
