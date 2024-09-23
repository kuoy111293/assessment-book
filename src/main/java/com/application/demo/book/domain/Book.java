package com.application.demo.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Book.
 */
@Entity
@Table(name = "book")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Book implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "author", nullable = false)
    private String author;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "book")
    @JsonIgnoreProperties(value = { "book" }, allowSetters = true)
    private Set<BookRegistration> bookRegistrations = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "books" }, allowSetters = true)
    private CategoryType categoryType;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Book id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthor() {
        return this.author;
    }

    public Book author(String author) {
        this.setAuthor(author);
        return this;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return this.title;
    }

    public Book title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public Book description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<BookRegistration> getBookRegistrations() {
        return this.bookRegistrations;
    }

    public void setBookRegistrations(Set<BookRegistration> bookRegistrations) {
        if (this.bookRegistrations != null) {
            this.bookRegistrations.forEach(i -> i.setBook(null));
        }
        if (bookRegistrations != null) {
            bookRegistrations.forEach(i -> i.setBook(this));
        }
        this.bookRegistrations = bookRegistrations;
    }

    public Book bookRegistrations(Set<BookRegistration> bookRegistrations) {
        this.setBookRegistrations(bookRegistrations);
        return this;
    }

    public Book addBookRegistration(BookRegistration bookRegistration) {
        this.bookRegistrations.add(bookRegistration);
        bookRegistration.setBook(this);
        return this;
    }

    public Book removeBookRegistration(BookRegistration bookRegistration) {
        this.bookRegistrations.remove(bookRegistration);
        bookRegistration.setBook(null);
        return this;
    }

    public CategoryType getCategoryType() {
        return this.categoryType;
    }

    public void setCategoryType(CategoryType categoryType) {
        this.categoryType = categoryType;
    }

    public Book categoryType(CategoryType categoryType) {
        this.setCategoryType(categoryType);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Book)) {
            return false;
        }
        return getId() != null && getId().equals(((Book) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Book{" +
            "id=" + getId() +
            ", author='" + getAuthor() + "'" +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
