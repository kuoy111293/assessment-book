package com.application.demo.book.domain;

import com.application.demo.book.domain.enumeration.BookStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A BookRegistration.
 */
@Entity
@Table(name = "book_registration")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class BookRegistration implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "student_id")
    private String studentId;

    @Column(name = "request_date")
    private ZonedDateTime requestDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "request_status")
    private BookStatus requestStatus;

    @Column(name = "return_date")
    private ZonedDateTime returnDate;

    @Size(max = 500)
    @Column(name = "remarks", length = 500)
    private String remarks;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "bookRegistrations", "categoryType" }, allowSetters = true)
    private Book book;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public BookRegistration id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public BookRegistration studentId(String studentId) {
        this.setStudentId(studentId);
        return this;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public ZonedDateTime getRequestDate() {
        return this.requestDate;
    }

    public BookRegistration requestDate(ZonedDateTime requestDate) {
        this.setRequestDate(requestDate);
        return this;
    }

    public void setRequestDate(ZonedDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public BookStatus getRequestStatus() {
        return this.requestStatus;
    }

    public BookRegistration requestStatus(BookStatus requestStatus) {
        this.setRequestStatus(requestStatus);
        return this;
    }

    public void setRequestStatus(BookStatus requestStatus) {
        this.requestStatus = requestStatus;
    }

    public ZonedDateTime getReturnDate() {
        return this.returnDate;
    }

    public BookRegistration returnDate(ZonedDateTime returnDate) {
        this.setReturnDate(returnDate);
        return this;
    }

    public void setReturnDate(ZonedDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public String getRemarks() {
        return this.remarks;
    }

    public BookRegistration remarks(String remarks) {
        this.setRemarks(remarks);
        return this;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public Book getBook() {
        return this.book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public BookRegistration book(Book book) {
        this.setBook(book);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BookRegistration)) {
            return false;
        }
        return getId() != null && getId().equals(((BookRegistration) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "BookRegistration{" +
            "id=" + getId() +
            ", studentId='" + getStudentId() + "'" +
            ", requestDate='" + getRequestDate() + "'" +
            ", requestStatus='" + getRequestStatus() + "'" +
            ", returnDate='" + getReturnDate() + "'" +
            ", remarks='" + getRemarks() + "'" +
            "}";
    }
}
