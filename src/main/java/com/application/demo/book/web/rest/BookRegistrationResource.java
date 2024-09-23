package com.application.demo.book.web.rest;

import com.application.demo.book.domain.Book;
import com.application.demo.book.domain.BookRegistration;
import com.application.demo.book.repository.BookRegistrationRepository;
import com.application.demo.book.repository.BookRepository;
import com.application.demo.book.web.rest.base.BaseResponse;
import com.application.demo.book.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.swing.text.html.Option;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.application.demo.book.domain.BookRegistration}.
 */
@RestController
@RequestMapping("/api/book-registrations")
@Transactional
public class BookRegistrationResource {

    private final Logger log = LoggerFactory.getLogger(BookRegistrationResource.class);

    private static final String ENTITY_NAME = "bookBookRegistration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BookRegistrationRepository bookRegistrationRepository;
    private final BookRepository bookRepository;

    public BookRegistrationResource(BookRegistrationRepository bookRegistrationRepository, BookRepository bookRepository) {
        this.bookRegistrationRepository = bookRegistrationRepository;
        this.bookRepository = bookRepository;
    }

    /**
     * {@code POST  /book-registrations} : Create a new bookRegistration.
     *
     * @param bookRegistration the bookRegistration to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookRegistration, or with status {@code 400 (Bad Request)} if the bookRegistration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<?> createBookRegistration(
        @RequestHeader HttpHeaders headers,
        @Valid @RequestBody BookRegistration bookRegistration
    ) throws URISyntaxException {
        log.debug("REST request to save BookRegistration : {}", bookRegistration);
        if (bookRegistration.getId() != null) {
            throw new BadRequestAlertException("A new bookRegistration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BookRegistration result = bookRegistrationRepository.save(bookRegistration);
        return new ResponseEntity<>(new BaseResponse<>(true, "Created successfully.", result), HttpStatus.CREATED);
    }

    /**
     * {@code PUT  /book-registrations/:id} : Updates an existing bookRegistration.
     *
     * @param id the id of the bookRegistration to save.
     * @param bookRegistration the bookRegistration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookRegistration,
     * or with status {@code 400 (Bad Request)} if the bookRegistration is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookRegistration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBookRegistration(
        @RequestHeader HttpHeaders headers,
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookRegistration bookRegistration
    ) throws URISyntaxException {
        log.debug("REST request to update BookRegistration : {}, {}", id, bookRegistration);
        if (bookRegistration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookRegistration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookRegistrationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        BookRegistration result = bookRegistrationRepository.save(bookRegistration);
        return new ResponseEntity<>(new BaseResponse<>(true, "Updated successfully.", result), HttpStatus.OK);
    }

    /**
     * {@code PATCH  /book-registrations/:id} : Partial updates given fields of an existing bookRegistration, field will ignore if it is null
     *
     * @param id the id of the bookRegistration to save.
     * @param bookRegistration the bookRegistration to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookRegistration,
     * or with status {@code 400 (Bad Request)} if the bookRegistration is not valid,
     * or with status {@code 404 (Not Found)} if the bookRegistration is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookRegistration couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<?> partialUpdateBookRegistration(
        @RequestHeader HttpHeaders headers,
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookRegistration bookRegistration
    ) throws URISyntaxException {
        log.debug("REST request to partial update BookRegistration partially : {}, {}", id, bookRegistration);
        if (bookRegistration.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookRegistration.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookRegistrationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Book> book = bookRepository.findById(id);

        Optional<BookRegistration> result = bookRegistrationRepository
            .findById(bookRegistration.getId())
            .map(existingBookRegistration -> {
                if (bookRegistration.getBook().getId() != null) {
                    existingBookRegistration.setBook(book.get());
                }
                if (bookRegistration.getStudentId() != null) {
                    existingBookRegistration.setStudentId(bookRegistration.getStudentId());
                }
                if (bookRegistration.getRequestDate() != null) {
                    existingBookRegistration.setRequestDate(bookRegistration.getRequestDate());
                }
                if (bookRegistration.getRequestStatus() != null) {
                    existingBookRegistration.setRequestStatus(bookRegistration.getRequestStatus());
                }
                if (bookRegistration.getReturnDate() != null) {
                    existingBookRegistration.setReturnDate(bookRegistration.getReturnDate());
                }
                if (bookRegistration.getRemarks() != null) {
                    existingBookRegistration.setRemarks(bookRegistration.getRemarks());
                }

                return existingBookRegistration;
            })
            .map(bookRegistrationRepository::save);

        return new ResponseEntity<>(new BaseResponse<>(true, "Inquiry successfully.", result), HttpStatus.OK);
    }

    /**
     * {@code GET  /book-registrations} : get all the bookRegistrations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookRegistrations in body.
     */
    @GetMapping("")
    public ResponseEntity<?> getAllBookRegistrations(@RequestHeader HttpHeaders headers) {
        log.debug("REST request to get all BookRegistrations");
        return new ResponseEntity<>(new BaseResponse<>(true, "Inquiry successfully.", bookRegistrationRepository.findAll()), HttpStatus.OK);
    }

    /**
     * {@code GET  /book-registrations/:id} : get the "id" bookRegistration.
     *
     * @param id the id of the bookRegistration to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookRegistration, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookRegistration(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        log.debug("REST request to get BookRegistration : {}", id);
        Optional<BookRegistration> bookRegistration = bookRegistrationRepository.findById(id);
        //return ResponseUtil.wrapOrNotFound(bookRegistration);
        return new ResponseEntity<>(new BaseResponse<>(true, "Inquiry successfully", bookRegistration), HttpStatus.OK);
    }

    /**
     * {@code DELETE  /book-registrations/:id} : delete the "id" bookRegistration.
     *
     * @param id the id of the bookRegistration to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookRegistration(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        log.debug("REST request to delete BookRegistration : {}", id);
        bookRegistrationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
