package com.application.demo.book.domain;

import static com.application.demo.book.domain.BookTestSamples.*;
import static com.application.demo.book.domain.CategoryTypeTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.application.demo.book.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CategoryTypeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CategoryType.class);
        CategoryType categoryType1 = getCategoryTypeSample1();
        CategoryType categoryType2 = new CategoryType();
        assertThat(categoryType1).isNotEqualTo(categoryType2);

        categoryType2.setId(categoryType1.getId());
        assertThat(categoryType1).isEqualTo(categoryType2);

        categoryType2 = getCategoryTypeSample2();
        assertThat(categoryType1).isNotEqualTo(categoryType2);
    }

    @Test
    void bookTest() throws Exception {
        CategoryType categoryType = getCategoryTypeRandomSampleGenerator();
        Book bookBack = getBookRandomSampleGenerator();

        categoryType.addBook(bookBack);
        assertThat(categoryType.getBooks()).containsOnly(bookBack);
        assertThat(bookBack.getCategoryType()).isEqualTo(categoryType);

        categoryType.removeBook(bookBack);
        assertThat(categoryType.getBooks()).doesNotContain(bookBack);
        assertThat(bookBack.getCategoryType()).isNull();

        categoryType.books(new HashSet<>(Set.of(bookBack)));
        assertThat(categoryType.getBooks()).containsOnly(bookBack);
        assertThat(bookBack.getCategoryType()).isEqualTo(categoryType);

        categoryType.setBooks(new HashSet<>());
        assertThat(categoryType.getBooks()).doesNotContain(bookBack);
        assertThat(bookBack.getCategoryType()).isNull();
    }
}
