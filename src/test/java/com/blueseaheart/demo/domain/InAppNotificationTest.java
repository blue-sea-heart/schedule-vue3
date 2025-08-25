package com.blueseaheart.demo.domain;

import static com.blueseaheart.demo.domain.InAppNotificationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.blueseaheart.demo.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InAppNotificationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InAppNotification.class);
        InAppNotification inAppNotification1 = getInAppNotificationSample1();
        InAppNotification inAppNotification2 = new InAppNotification();
        assertThat(inAppNotification1).isNotEqualTo(inAppNotification2);

        inAppNotification2.setId(inAppNotification1.getId());
        assertThat(inAppNotification1).isEqualTo(inAppNotification2);

        inAppNotification2 = getInAppNotificationSample2();
        assertThat(inAppNotification1).isNotEqualTo(inAppNotification2);
    }
}
