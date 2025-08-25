package com.blueseaheart.demo.domain;

import static com.blueseaheart.demo.domain.ViewPreferenceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blueseaheart.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ViewPreferenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ViewPreference.class);
        ViewPreference viewPreference1 = getViewPreferenceSample1();
        ViewPreference viewPreference2 = new ViewPreference();
        assertThat(viewPreference1).isNotEqualTo(viewPreference2);

        viewPreference2.setId(viewPreference1.getId());
        assertThat(viewPreference1).isEqualTo(viewPreference2);

        viewPreference2 = getViewPreferenceSample2();
        assertThat(viewPreference1).isNotEqualTo(viewPreference2);
    }
}
