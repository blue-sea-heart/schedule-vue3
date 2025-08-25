package com.blueseaheart.demo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.blueseaheart.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ViewPreferenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ViewPreferenceDTO.class);
        ViewPreferenceDTO viewPreferenceDTO1 = new ViewPreferenceDTO();
        viewPreferenceDTO1.setId(1L);
        ViewPreferenceDTO viewPreferenceDTO2 = new ViewPreferenceDTO();
        assertThat(viewPreferenceDTO1).isNotEqualTo(viewPreferenceDTO2);
        viewPreferenceDTO2.setId(viewPreferenceDTO1.getId());
        assertThat(viewPreferenceDTO1).isEqualTo(viewPreferenceDTO2);
        viewPreferenceDTO2.setId(2L);
        assertThat(viewPreferenceDTO1).isNotEqualTo(viewPreferenceDTO2);
        viewPreferenceDTO1.setId(null);
        assertThat(viewPreferenceDTO1).isNotEqualTo(viewPreferenceDTO2);
    }
}
