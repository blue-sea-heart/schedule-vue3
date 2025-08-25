package com.blueseaheart.demo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.blueseaheart.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ScheduleStatusDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ScheduleStatusDTO.class);
        ScheduleStatusDTO scheduleStatusDTO1 = new ScheduleStatusDTO();
        scheduleStatusDTO1.setId(1L);
        ScheduleStatusDTO scheduleStatusDTO2 = new ScheduleStatusDTO();
        assertThat(scheduleStatusDTO1).isNotEqualTo(scheduleStatusDTO2);
        scheduleStatusDTO2.setId(scheduleStatusDTO1.getId());
        assertThat(scheduleStatusDTO1).isEqualTo(scheduleStatusDTO2);
        scheduleStatusDTO2.setId(2L);
        assertThat(scheduleStatusDTO1).isNotEqualTo(scheduleStatusDTO2);
        scheduleStatusDTO1.setId(null);
        assertThat(scheduleStatusDTO1).isNotEqualTo(scheduleStatusDTO2);
    }
}
