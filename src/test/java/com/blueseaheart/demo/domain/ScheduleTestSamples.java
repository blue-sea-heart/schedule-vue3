package com.blueseaheart.demo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Schedule getScheduleSample1() {
        return new Schedule().id(1L).title("title1").location("location1");
    }

    public static Schedule getScheduleSample2() {
        return new Schedule().id(2L).title("title2").location("location2");
    }

    public static Schedule getScheduleRandomSampleGenerator() {
        return new Schedule().id(longCount.incrementAndGet()).title(UUID.randomUUID().toString()).location(UUID.randomUUID().toString());
    }
}
