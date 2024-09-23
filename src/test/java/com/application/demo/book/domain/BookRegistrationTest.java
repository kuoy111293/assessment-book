package com.application.demo.book.domain;

import static com.application.demo.book.domain.BookRegistrationTestSamples.*;
import static com.application.demo.book.domain.BookTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.application.demo.book.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class BookRegistrationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BookRegistration.class);
        BookRegistration bookRegistration1 = getBookRegistrationSample1();
        BookRegistration bookRegistration2 = new BookRegistration();
        assertThat(bookRegistration1).isNotEqualTo(bookRegistration2);

        bookRegistration2.setId(bookRegistration1.getId());
        assertThat(bookRegistration1).isEqualTo(bookRegistration2);

        bookRegistration2 = getBookRegistrationSample2();
        assertThat(bookRegistration1).isNotEqualTo(bookRegistration2);
    }

    @Test
    void bookTest() throws Exception {
        BookRegistration bookRegistration = getBookRegistrationRandomSampleGenerator();
        Book bookBack = getBookRandomSampleGenerator();

        bookRegistration.setBook(bookBack);
        assertThat(bookRegistration.getBook()).isEqualTo(bookBack);

        bookRegistration.book(null);
        assertThat(bookRegistration.getBook()).isNull();
    }
}
