package com.application.demo.book.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class CategoryTypeTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static CategoryType getCategoryTypeSample1() {
        return new CategoryType().id(1L).title("title1").description("description1");
    }

    public static CategoryType getCategoryTypeSample2() {
        return new CategoryType().id(2L).title("title2").description("description2");
    }

    public static CategoryType getCategoryTypeRandomSampleGenerator() {
        return new CategoryType()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
