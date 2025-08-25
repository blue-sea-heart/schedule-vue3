package com.blueseaheart.demo.domain;

import static com.blueseaheart.demo.domain.ScheduleTestSamples.*;
import static com.blueseaheart.demo.domain.TagTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blueseaheart.demo.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class TagTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Tag.class);
        Tag tag1 = getTagSample1();
        Tag tag2 = new Tag();
        assertThat(tag1).isNotEqualTo(tag2);

        tag2.setId(tag1.getId());
        assertThat(tag1).isEqualTo(tag2);

        tag2 = getTagSample2();
        assertThat(tag1).isNotEqualTo(tag2);
    }

    @Test
    void schedulesTest() {
        Tag tag = getTagRandomSampleGenerator();
        Schedule scheduleBack = getScheduleRandomSampleGenerator();

        tag.addSchedules(scheduleBack);
        assertThat(tag.getSchedules()).containsOnly(scheduleBack);
        assertThat(scheduleBack.getTags()).containsOnly(tag);

        tag.removeSchedules(scheduleBack);
        assertThat(tag.getSchedules()).doesNotContain(scheduleBack);
        assertThat(scheduleBack.getTags()).doesNotContain(tag);

        tag.schedules(new HashSet<>(Set.of(scheduleBack)));
        assertThat(tag.getSchedules()).containsOnly(scheduleBack);
        assertThat(scheduleBack.getTags()).containsOnly(tag);

        tag.setSchedules(new HashSet<>());
        assertThat(tag.getSchedules()).doesNotContain(scheduleBack);
        assertThat(scheduleBack.getTags()).doesNotContain(tag);
    }
}
