package com.blueseaheart.demo.service.mapper;

import static com.blueseaheart.demo.domain.ScheduleStatusAsserts.*;
import static com.blueseaheart.demo.domain.ScheduleStatusTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScheduleStatusMapperTest {

    private ScheduleStatusMapper scheduleStatusMapper;

    @BeforeEach
    void setUp() {
        scheduleStatusMapper = new ScheduleStatusMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getScheduleStatusSample1();
        var actual = scheduleStatusMapper.toEntity(scheduleStatusMapper.toDto(expected));
        assertScheduleStatusAllPropertiesEquals(expected, actual);
    }
}
