package com.application.demo.book.domain;

import static com.application.demo.book.domain.BookRegistrationTestSamples.*;
import static com.application.demo.book.domain.BookTestSamples.*;
import static com.application.demo.book.domain.CategoryTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.application.demo.book.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Book.class);
        Book book1 = getBookSample1();
        Book book2 = new Book();
        assertThat(book1).isNotEqualTo(book2);

        book2.setId(book1.getId());
        assertThat(book1).isEqualTo(book2);

        book2 = getBookSample2();
        assertThat(book1).isNotEqualTo(book2);
    }

    @Test
    void bookRegistrationTest() throws Exception {
        Book book = getBookRandomSampleGenerator();
        BookRegistration bookRegistrationBack = getBookRegistrationRandomSampleGenerator();

        book.addBookRegistration(bookRegistrationBack);
        assertThat(book.getBookRegistrations()).containsOnly(bookRegistrationBack);
        assertThat(bookRegistrationBack.getBook()).isEqualTo(book);

        book.removeBookRegistration(bookRegistrationBack);
        assertThat(book.getBookRegistrations()).doesNotContain(bookRegistrationBack);
        assertThat(bookRegistrationBack.getBook()).isNull();

        book.bookRegistrations(new HashSet<>(Set.of(bookRegistrationBack)));
        assertThat(book.getBookRegistrations()).containsOnly(bookRegistrationBack);
        assertThat(bookRegistrationBack.getBook()).isEqualTo(book);

        book.setBookRegistrations(new HashSet<>());
        assertThat(book.getBookRegistrations()).doesNotContain(bookRegistrationBack);
        assertThat(bookRegistrationBack.getBook()).isNull();
    }

    @Test
    void categoryTypeTest() throws Exception {
        Book book = getBookRandomSampleGenerator();
        CategoryType categoryTypeBack = getCategoryTypeRandomSampleGenerator();

        book.setCategoryType(categoryTypeBack);
        assertThat(book.getCategoryType()).isEqualTo(categoryTypeBack);

        book.categoryType(null);
        assertThat(book.getCategoryType()).isNull();
    }
}
