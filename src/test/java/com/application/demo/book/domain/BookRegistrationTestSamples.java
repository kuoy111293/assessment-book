package com.application.demo.book.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class BookRegistrationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static BookRegistration getBookRegistrationSample1() {
        return new BookRegistration().id(1L).bookId(1L).studentId("studentId1").remarks("remarks1");
    }

    public static BookRegistration getBookRegistrationSample2() {
        return new BookRegistration().id(2L).bookId(2L).studentId("studentId2").remarks("remarks2");
    }

    public static BookRegistration getBookRegistrationRandomSampleGenerator() {
        return new BookRegistration()
            .id(longCount.incrementAndGet())
            .bookId(longCount.incrementAndGet())
            .studentId(UUID.randomUUID().toString())
            .remarks(UUID.randomUUID().toString());
    }
}
