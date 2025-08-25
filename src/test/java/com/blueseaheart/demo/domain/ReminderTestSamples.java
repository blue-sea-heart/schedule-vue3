package com.blueseaheart.demo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ReminderTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Reminder getReminderSample1() {
        return new Reminder().id(1L).subject("subject1").attemptCount(1).lastErrorMsg("lastErrorMsg1").errorMsg("errorMsg1");
    }

    public static Reminder getReminderSample2() {
        return new Reminder().id(2L).subject("subject2").attemptCount(2).lastErrorMsg("lastErrorMsg2").errorMsg("errorMsg2");
    }

    public static Reminder getReminderRandomSampleGenerator() {
        return new Reminder()
            .id(longCount.incrementAndGet())
            .subject(UUID.randomUUID().toString())
            .attemptCount(intCount.incrementAndGet())
            .lastErrorMsg(UUID.randomUUID().toString())
            .errorMsg(UUID.randomUUID().toString());
    }
}
