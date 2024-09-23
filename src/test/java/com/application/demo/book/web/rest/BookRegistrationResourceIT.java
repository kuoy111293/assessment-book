package com.application.demo.book.web.rest;

import static com.application.demo.book.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.application.demo.book.IntegrationTest;
import com.application.demo.book.domain.BookRegistration;
import com.application.demo.book.domain.enumeration.BookStatus;
import com.application.demo.book.repository.BookRegistrationRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link BookRegistrationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookRegistrationResourceIT {

    private static final Long DEFAULT_BOOK_ID = 1L;
    private static final Long UPDATED_BOOK_ID = 2L;

    private static final String DEFAULT_STUDENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_STUDENT_ID = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_REQUEST_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REQUEST_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final BookStatus DEFAULT_REQUEST_STATUS = BookStatus.BORROW;
    private static final BookStatus UPDATED_REQUEST_STATUS = BookStatus.CANCEL;

    private static final ZonedDateTime DEFAULT_RETURN_DATE = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_RETURN_DATE = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_REMARKS = "AAAAAAAAAA";
    private static final String UPDATED_REMARKS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/book-registrations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private BookRegistrationRepository bookRegistrationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookRegistrationMockMvc;

    private BookRegistration bookRegistration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookRegistration createEntity(EntityManager em) {
        BookRegistration bookRegistration = new BookRegistration()
            .studentId(DEFAULT_STUDENT_ID)
            .requestDate(DEFAULT_REQUEST_DATE)
            .requestStatus(DEFAULT_REQUEST_STATUS)
            .returnDate(DEFAULT_RETURN_DATE)
            .remarks(DEFAULT_REMARKS);
        return bookRegistration;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BookRegistration createUpdatedEntity(EntityManager em) {
        BookRegistration bookRegistration = new BookRegistration()
            .studentId(UPDATED_STUDENT_ID)
            .requestDate(UPDATED_REQUEST_DATE)
            .requestStatus(UPDATED_REQUEST_STATUS)
            .returnDate(UPDATED_RETURN_DATE)
            .remarks(UPDATED_REMARKS);
        return bookRegistration;
    }

    @BeforeEach
    public void initTest() {
        bookRegistration = createEntity(em);
    }

    @Test
    @Transactional
    void createBookRegistration() throws Exception {
        int databaseSizeBeforeCreate = bookRegistrationRepository.findAll().size();
        // Create the BookRegistration
        restBookRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookRegistration))
            )
            .andExpect(status().isCreated());

        // Validate the BookRegistration in the database
        List<BookRegistration> bookRegistrationList = bookRegistrationRepository.findAll();
        assertThat(bookRegistrationList).hasSize(databaseSizeBeforeCreate + 1);
        BookRegistration testBookRegistration = bookRegistrationList.get(bookRegistrationList.size() - 1);
        assertThat(testBookRegistration.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testBookRegistration.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
        assertThat(testBookRegistration.getRequestStatus()).isEqualTo(DEFAULT_REQUEST_STATUS);
        assertThat(testBookRegistration.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
        assertThat(testBookRegistration.getRemarks()).isEqualTo(DEFAULT_REMARKS);
    }

    @Test
    @Transactional
    void createBookRegistrationWithExistingId() throws Exception {
        // Create the BookRegistration with an existing ID
        bookRegistration.setId(1L);

        int databaseSizeBeforeCreate = bookRegistrationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookRegistrationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookRegistration))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookRegistration in the database
        List<BookRegistration> bookRegistrationList = bookRegistrationRepository.findAll();
        assertThat(bookRegistrationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllBookRegistrations() throws Exception {
        // Initialize the database
        bookRegistrationRepository.saveAndFlush(bookRegistration);

        // Get all the bookRegistrationList
        restBookRegistrationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookRegistration.getId().intValue())))
            .andExpect(jsonPath("$.[*].bookId").value(hasItem(DEFAULT_BOOK_ID.intValue())))
            .andExpect(jsonPath("$.[*].studentId").value(hasItem(DEFAULT_STUDENT_ID)))
            .andExpect(jsonPath("$.[*].requestDate").value(hasItem(sameInstant(DEFAULT_REQUEST_DATE))))
            .andExpect(jsonPath("$.[*].requestStatus").value(hasItem(DEFAULT_REQUEST_STATUS.toString())))
            .andExpect(jsonPath("$.[*].returnDate").value(hasItem(sameInstant(DEFAULT_RETURN_DATE))))
            .andExpect(jsonPath("$.[*].remarks").value(hasItem(DEFAULT_REMARKS)));
    }

    @Test
    @Transactional
    void getBookRegistration() throws Exception {
        // Initialize the database
        bookRegistrationRepository.saveAndFlush(bookRegistration);

        // Get the bookRegistration
        restBookRegistrationMockMvc
            .perform(get(ENTITY_API_URL_ID, bookRegistration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookRegistration.getId().intValue()))
            .andExpect(jsonPath("$.bookId").value(DEFAULT_BOOK_ID.intValue()))
            .andExpect(jsonPath("$.studentId").value(DEFAULT_STUDENT_ID))
            .andExpect(jsonPath("$.requestDate").value(sameInstant(DEFAULT_REQUEST_DATE)))
            .andExpect(jsonPath("$.requestStatus").value(DEFAULT_REQUEST_STATUS.toString()))
            .andExpect(jsonPath("$.returnDate").value(sameInstant(DEFAULT_RETURN_DATE)))
            .andExpect(jsonPath("$.remarks").value(DEFAULT_REMARKS));
    }

    @Test
    @Transactional
    void getNonExistingBookRegistration() throws Exception {
        // Get the bookRegistration
        restBookRegistrationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookRegistration() throws Exception {
        // Initialize the database
        bookRegistrationRepository.saveAndFlush(bookRegistration);

        int databaseSizeBeforeUpdate = bookRegistrationRepository.findAll().size();

        // Update the bookRegistration
        BookRegistration updatedBookRegistration = bookRegistrationRepository.findById(bookRegistration.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookRegistration are not directly saved in db
        em.detach(updatedBookRegistration);
        updatedBookRegistration
            .studentId(UPDATED_STUDENT_ID)
            .requestDate(UPDATED_REQUEST_DATE)
            .requestStatus(UPDATED_REQUEST_STATUS)
            .returnDate(UPDATED_RETURN_DATE)
            .remarks(UPDATED_REMARKS);

        restBookRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedBookRegistration.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedBookRegistration))
            )
            .andExpect(status().isOk());

        // Validate the BookRegistration in the database
        List<BookRegistration> bookRegistrationList = bookRegistrationRepository.findAll();
        assertThat(bookRegistrationList).hasSize(databaseSizeBeforeUpdate);
        BookRegistration testBookRegistration = bookRegistrationList.get(bookRegistrationList.size() - 1);
        assertThat(testBookRegistration.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testBookRegistration.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testBookRegistration.getRequestStatus()).isEqualTo(UPDATED_REQUEST_STATUS);
        assertThat(testBookRegistration.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
        assertThat(testBookRegistration.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void putNonExistingBookRegistration() throws Exception {
        int databaseSizeBeforeUpdate = bookRegistrationRepository.findAll().size();
        bookRegistration.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookRegistration.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookRegistration))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookRegistration in the database
        List<BookRegistration> bookRegistrationList = bookRegistrationRepository.findAll();
        assertThat(bookRegistrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookRegistration() throws Exception {
        int databaseSizeBeforeUpdate = bookRegistrationRepository.findAll().size();
        bookRegistration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(bookRegistration))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookRegistration in the database
        List<BookRegistration> bookRegistrationList = bookRegistrationRepository.findAll();
        assertThat(bookRegistrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookRegistration() throws Exception {
        int databaseSizeBeforeUpdate = bookRegistrationRepository.findAll().size();
        bookRegistration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookRegistrationMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(bookRegistration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookRegistration in the database
        List<BookRegistration> bookRegistrationList = bookRegistrationRepository.findAll();
        assertThat(bookRegistrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookRegistrationWithPatch() throws Exception {
        // Initialize the database
        bookRegistrationRepository.saveAndFlush(bookRegistration);

        int databaseSizeBeforeUpdate = bookRegistrationRepository.findAll().size();

        // Update the bookRegistration using partial update
        BookRegistration partialUpdatedBookRegistration = new BookRegistration();
        partialUpdatedBookRegistration.setId(bookRegistration.getId());

        partialUpdatedBookRegistration.remarks(UPDATED_REMARKS);

        restBookRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookRegistration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookRegistration))
            )
            .andExpect(status().isOk());

        // Validate the BookRegistration in the database
        List<BookRegistration> bookRegistrationList = bookRegistrationRepository.findAll();
        assertThat(bookRegistrationList).hasSize(databaseSizeBeforeUpdate);
        BookRegistration testBookRegistration = bookRegistrationList.get(bookRegistrationList.size() - 1);
        assertThat(testBookRegistration.getStudentId()).isEqualTo(DEFAULT_STUDENT_ID);
        assertThat(testBookRegistration.getRequestDate()).isEqualTo(DEFAULT_REQUEST_DATE);
        assertThat(testBookRegistration.getRequestStatus()).isEqualTo(DEFAULT_REQUEST_STATUS);
        assertThat(testBookRegistration.getReturnDate()).isEqualTo(DEFAULT_RETURN_DATE);
        assertThat(testBookRegistration.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void fullUpdateBookRegistrationWithPatch() throws Exception {
        // Initialize the database
        bookRegistrationRepository.saveAndFlush(bookRegistration);

        int databaseSizeBeforeUpdate = bookRegistrationRepository.findAll().size();

        // Update the bookRegistration using partial update
        BookRegistration partialUpdatedBookRegistration = new BookRegistration();
        partialUpdatedBookRegistration.setId(bookRegistration.getId());

        partialUpdatedBookRegistration
            .studentId(UPDATED_STUDENT_ID)
            .requestDate(UPDATED_REQUEST_DATE)
            .requestStatus(UPDATED_REQUEST_STATUS)
            .returnDate(UPDATED_RETURN_DATE)
            .remarks(UPDATED_REMARKS);

        restBookRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookRegistration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedBookRegistration))
            )
            .andExpect(status().isOk());

        // Validate the BookRegistration in the database
        List<BookRegistration> bookRegistrationList = bookRegistrationRepository.findAll();
        assertThat(bookRegistrationList).hasSize(databaseSizeBeforeUpdate);
        BookRegistration testBookRegistration = bookRegistrationList.get(bookRegistrationList.size() - 1);
        assertThat(testBookRegistration.getStudentId()).isEqualTo(UPDATED_STUDENT_ID);
        assertThat(testBookRegistration.getRequestDate()).isEqualTo(UPDATED_REQUEST_DATE);
        assertThat(testBookRegistration.getRequestStatus()).isEqualTo(UPDATED_REQUEST_STATUS);
        assertThat(testBookRegistration.getReturnDate()).isEqualTo(UPDATED_RETURN_DATE);
        assertThat(testBookRegistration.getRemarks()).isEqualTo(UPDATED_REMARKS);
    }

    @Test
    @Transactional
    void patchNonExistingBookRegistration() throws Exception {
        int databaseSizeBeforeUpdate = bookRegistrationRepository.findAll().size();
        bookRegistration.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookRegistration.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookRegistration))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookRegistration in the database
        List<BookRegistration> bookRegistrationList = bookRegistrationRepository.findAll();
        assertThat(bookRegistrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookRegistration() throws Exception {
        int databaseSizeBeforeUpdate = bookRegistrationRepository.findAll().size();
        bookRegistration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookRegistration))
            )
            .andExpect(status().isBadRequest());

        // Validate the BookRegistration in the database
        List<BookRegistration> bookRegistrationList = bookRegistrationRepository.findAll();
        assertThat(bookRegistrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookRegistration() throws Exception {
        int databaseSizeBeforeUpdate = bookRegistrationRepository.findAll().size();
        bookRegistration.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookRegistrationMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(bookRegistration))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the BookRegistration in the database
        List<BookRegistration> bookRegistrationList = bookRegistrationRepository.findAll();
        assertThat(bookRegistrationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookRegistration() throws Exception {
        // Initialize the database
        bookRegistrationRepository.saveAndFlush(bookRegistration);

        int databaseSizeBeforeDelete = bookRegistrationRepository.findAll().size();

        // Delete the bookRegistration
        restBookRegistrationMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookRegistration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BookRegistration> bookRegistrationList = bookRegistrationRepository.findAll();
        assertThat(bookRegistrationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
