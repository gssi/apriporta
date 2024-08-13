package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AccessRuleTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AccessRule getAccessRuleSample1() {
        return new AccessRule().id(1L).notes("notes1");
    }

    public static AccessRule getAccessRuleSample2() {
        return new AccessRule().id(2L).notes("notes2");
    }

    public static AccessRule getAccessRuleRandomSampleGenerator() {
        return new AccessRule().id(longCount.incrementAndGet()).notes(UUID.randomUUID().toString());
    }
}
