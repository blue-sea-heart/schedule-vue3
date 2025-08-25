package com.blueseaheart.demo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InAppNotificationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InAppNotification getInAppNotificationSample1() {
        return new InAppNotification().id(1L).title("title1");
    }

    public static InAppNotification getInAppNotificationSample2() {
        return new InAppNotification().id(2L).title("title2");
    }

    public static InAppNotification getInAppNotificationRandomSampleGenerator() {
        return new InAppNotification().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString());
    }
}
