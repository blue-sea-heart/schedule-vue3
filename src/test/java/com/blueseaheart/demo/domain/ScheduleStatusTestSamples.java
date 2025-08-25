package com.blueseaheart.demo.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ScheduleStatusTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ScheduleStatus getScheduleStatusSample1() {
        return new ScheduleStatus().id(1L).code("code1").name("name1").color("color1").sortNo(1);
    }

    public static ScheduleStatus getScheduleStatusSample2() {
        return new ScheduleStatus().id(2L).code("code2").name("name2").color("color2").sortNo(2);
    }

    public static ScheduleStatus getScheduleStatusRandomSampleGenerator() {
        return new ScheduleStatus()
            .id(longCount.incrementAndGet())
            .code(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .color(UUID.randomUUID().toString())
            .sortNo(intCount.incrementAndGet());
    }
}
