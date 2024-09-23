package com.application.demo.book.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.application.demo.book.IntegrationTest;
import com.application.demo.book.domain.CategoryType;
import com.application.demo.book.repository.CategoryTypeRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link CategoryTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CategoryTypeResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/category-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CategoryTypeRepository categoryTypeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCategoryTypeMockMvc;

    private CategoryType categoryType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryType createEntity(EntityManager em) {
        CategoryType categoryType = new CategoryType().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION);
        return categoryType;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CategoryType createUpdatedEntity(EntityManager em) {
        CategoryType categoryType = new CategoryType().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);
        return categoryType;
    }

    @BeforeEach
    public void initTest() {
        categoryType = createEntity(em);
    }

    @Test
    @Transactional
    void createCategoryType() throws Exception {
        int databaseSizeBeforeCreate = categoryTypeRepository.findAll().size();
        // Create the CategoryType
        restCategoryTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryType)))
            .andExpect(status().isCreated());

        // Validate the CategoryType in the database
        List<CategoryType> categoryTypeList = categoryTypeRepository.findAll();
        assertThat(categoryTypeList).hasSize(databaseSizeBeforeCreate + 1);
        CategoryType testCategoryType = categoryTypeList.get(categoryTypeList.size() - 1);
        assertThat(testCategoryType.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testCategoryType.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void createCategoryTypeWithExistingId() throws Exception {
        // Create the CategoryType with an existing ID
        categoryType.setId(1L);

        int databaseSizeBeforeCreate = categoryTypeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCategoryTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryType)))
            .andExpect(status().isBadRequest());

        // Validate the CategoryType in the database
        List<CategoryType> categoryTypeList = categoryTypeRepository.findAll();
        assertThat(categoryTypeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        int databaseSizeBeforeTest = categoryTypeRepository.findAll().size();
        // set the field null
        categoryType.setTitle(null);

        // Create the CategoryType, which fails.

        restCategoryTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryType)))
            .andExpect(status().isBadRequest());

        List<CategoryType> categoryTypeList = categoryTypeRepository.findAll();
        assertThat(categoryTypeList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCategoryTypes() throws Exception {
        // Initialize the database
        categoryTypeRepository.saveAndFlush(categoryType);

        // Get all the categoryTypeList
        restCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(categoryType.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @Test
    @Transactional
    void getCategoryType() throws Exception {
        // Initialize the database
        categoryTypeRepository.saveAndFlush(categoryType);

        // Get the categoryType
        restCategoryTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, categoryType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(categoryType.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingCategoryType() throws Exception {
        // Get the categoryType
        restCategoryTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCategoryType() throws Exception {
        // Initialize the database
        categoryTypeRepository.saveAndFlush(categoryType);

        int databaseSizeBeforeUpdate = categoryTypeRepository.findAll().size();

        // Update the categoryType
        CategoryType updatedCategoryType = categoryTypeRepository.findById(categoryType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCategoryType are not directly saved in db
        em.detach(updatedCategoryType);
        updatedCategoryType.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCategoryType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCategoryType))
            )
            .andExpect(status().isOk());

        // Validate the CategoryType in the database
        List<CategoryType> categoryTypeList = categoryTypeRepository.findAll();
        assertThat(categoryTypeList).hasSize(databaseSizeBeforeUpdate);
        CategoryType testCategoryType = categoryTypeList.get(categoryTypeList.size() - 1);
        assertThat(testCategoryType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCategoryType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void putNonExistingCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = categoryTypeRepository.findAll().size();
        categoryType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, categoryType.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryType in the database
        List<CategoryType> categoryTypeList = categoryTypeRepository.findAll();
        assertThat(categoryTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = categoryTypeRepository.findAll().size();
        categoryType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(categoryType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryType in the database
        List<CategoryType> categoryTypeList = categoryTypeRepository.findAll();
        assertThat(categoryTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = categoryTypeRepository.findAll().size();
        categoryType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(categoryType)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryType in the database
        List<CategoryType> categoryTypeList = categoryTypeRepository.findAll();
        assertThat(categoryTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCategoryTypeWithPatch() throws Exception {
        // Initialize the database
        categoryTypeRepository.saveAndFlush(categoryType);

        int databaseSizeBeforeUpdate = categoryTypeRepository.findAll().size();

        // Update the categoryType using partial update
        CategoryType partialUpdatedCategoryType = new CategoryType();
        partialUpdatedCategoryType.setId(categoryType.getId());

        partialUpdatedCategoryType.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoryType))
            )
            .andExpect(status().isOk());

        // Validate the CategoryType in the database
        List<CategoryType> categoryTypeList = categoryTypeRepository.findAll();
        assertThat(categoryTypeList).hasSize(databaseSizeBeforeUpdate);
        CategoryType testCategoryType = categoryTypeList.get(categoryTypeList.size() - 1);
        assertThat(testCategoryType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCategoryType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void fullUpdateCategoryTypeWithPatch() throws Exception {
        // Initialize the database
        categoryTypeRepository.saveAndFlush(categoryType);

        int databaseSizeBeforeUpdate = categoryTypeRepository.findAll().size();

        // Update the categoryType using partial update
        CategoryType partialUpdatedCategoryType = new CategoryType();
        partialUpdatedCategoryType.setId(categoryType.getId());

        partialUpdatedCategoryType.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION);

        restCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCategoryType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCategoryType))
            )
            .andExpect(status().isOk());

        // Validate the CategoryType in the database
        List<CategoryType> categoryTypeList = categoryTypeRepository.findAll();
        assertThat(categoryTypeList).hasSize(databaseSizeBeforeUpdate);
        CategoryType testCategoryType = categoryTypeList.get(categoryTypeList.size() - 1);
        assertThat(testCategoryType.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testCategoryType.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void patchNonExistingCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = categoryTypeRepository.findAll().size();
        categoryType.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, categoryType.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryType in the database
        List<CategoryType> categoryTypeList = categoryTypeRepository.findAll();
        assertThat(categoryTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = categoryTypeRepository.findAll().size();
        categoryType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(categoryType))
            )
            .andExpect(status().isBadRequest());

        // Validate the CategoryType in the database
        List<CategoryType> categoryTypeList = categoryTypeRepository.findAll();
        assertThat(categoryTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCategoryType() throws Exception {
        int databaseSizeBeforeUpdate = categoryTypeRepository.findAll().size();
        categoryType.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCategoryTypeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(categoryType))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CategoryType in the database
        List<CategoryType> categoryTypeList = categoryTypeRepository.findAll();
        assertThat(categoryTypeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCategoryType() throws Exception {
        // Initialize the database
        categoryTypeRepository.saveAndFlush(categoryType);

        int databaseSizeBeforeDelete = categoryTypeRepository.findAll().size();

        // Delete the categoryType
        restCategoryTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, categoryType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CategoryType> categoryTypeList = categoryTypeRepository.findAll();
        assertThat(categoryTypeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
