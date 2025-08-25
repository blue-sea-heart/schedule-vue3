package com.blueseaheart.demo.service.mapper;

import static com.blueseaheart.demo.domain.ViewPreferenceAsserts.*;
import static com.blueseaheart.demo.domain.ViewPreferenceTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ViewPreferenceMapperTest {

    private ViewPreferenceMapper viewPreferenceMapper;

    @BeforeEach
    void setUp() {
        viewPreferenceMapper = new ViewPreferenceMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getViewPreferenceSample1();
        var actual = viewPreferenceMapper.toEntity(viewPreferenceMapper.toDto(expected));
        assertViewPreferenceAllPropertiesEquals(expected, actual);
    }
}
