package com.blueseaheart.demo.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class ViewPreferenceTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ViewPreference getViewPreferenceSample1() {
        return new ViewPreference().id(1L);
    }

    public static ViewPreference getViewPreferenceSample2() {
        return new ViewPreference().id(2L);
    }

    public static ViewPreference getViewPreferenceRandomSampleGenerator() {
        return new ViewPreference().id(longCount.incrementAndGet());
    }
}
