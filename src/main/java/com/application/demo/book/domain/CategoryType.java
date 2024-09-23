package com.application.demo.book.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A CategoryType.
 */
@Entity
@Table(name = "category_type")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CategoryType implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @Size(max = 500)
    @Column(name = "description", length = 500)
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "categoryType")
    @JsonIgnoreProperties(value = { "bookRegistrations", "categoryType" }, allowSetters = true)
    private Set<Book> books = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public CategoryType id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public CategoryType title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public CategoryType description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Book> getBooks() {
        return this.books;
    }

    public void setBooks(Set<Book> books) {
        if (this.books != null) {
            this.books.forEach(i -> i.setCategoryType(null));
        }
        if (books != null) {
            books.forEach(i -> i.setCategoryType(this));
        }
        this.books = books;
    }

    public CategoryType books(Set<Book> books) {
        this.setBooks(books);
        return this;
    }

    public CategoryType addBook(Book book) {
        this.books.add(book);
        book.setCategoryType(this);
        return this;
    }

    public CategoryType removeBook(Book book) {
        this.books.remove(book);
        book.setCategoryType(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CategoryType)) {
            return false;
        }
        return getId() != null && getId().equals(((CategoryType) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CategoryType{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
