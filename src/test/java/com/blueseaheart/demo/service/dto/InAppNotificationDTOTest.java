package com.blueseaheart.demo.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.blueseaheart.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InAppNotificationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(InAppNotificationDTO.class);
        InAppNotificationDTO inAppNotificationDTO1 = new InAppNotificationDTO();
        inAppNotificationDTO1.setId(1L);
        InAppNotificationDTO inAppNotificationDTO2 = new InAppNotificationDTO();
        assertThat(inAppNotificationDTO1).isNotEqualTo(inAppNotificationDTO2);
        inAppNotificationDTO2.setId(inAppNotificationDTO1.getId());
        assertThat(inAppNotificationDTO1).isEqualTo(inAppNotificationDTO2);
        inAppNotificationDTO2.setId(2L);
        assertThat(inAppNotificationDTO1).isNotEqualTo(inAppNotificationDTO2);
        inAppNotificationDTO1.setId(null);
        assertThat(inAppNotificationDTO1).isNotEqualTo(inAppNotificationDTO2);
    }
}
