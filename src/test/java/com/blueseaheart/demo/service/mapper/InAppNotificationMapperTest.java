package com.blueseaheart.demo.service.mapper;

import static com.blueseaheart.demo.domain.InAppNotificationAsserts.*;
import static com.blueseaheart.demo.domain.InAppNotificationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class InAppNotificationMapperTest {

    private InAppNotificationMapper inAppNotificationMapper;

    @BeforeEach
    void setUp() {
        inAppNotificationMapper = new InAppNotificationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getInAppNotificationSample1();
        var actual = inAppNotificationMapper.toEntity(inAppNotificationMapper.toDto(expected));
        assertInAppNotificationAllPropertiesEquals(expected, actual);
    }
}
