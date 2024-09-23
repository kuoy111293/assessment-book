package com.application.demo.book.web.rest;

import com.application.demo.book.domain.CategoryType;
import com.application.demo.book.repository.CategoryTypeRepository;
import com.application.demo.book.web.rest.base.BaseResponse;
import com.application.demo.book.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
 * REST controller for managing {@link com.application.demo.book.domain.CategoryType}.
 */
@RestController
@RequestMapping("/api/category-types")
@Transactional
public class CategoryTypeResource {

    private final Logger log = LoggerFactory.getLogger(CategoryTypeResource.class);

    private static final String ENTITY_NAME = "bookCategoryType";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CategoryTypeRepository categoryTypeRepository;

    public CategoryTypeResource(CategoryTypeRepository categoryTypeRepository) {
        this.categoryTypeRepository = categoryTypeRepository;
    }

    /**
     * {@code POST  /category-types} : Create a new categoryType.
     *
     * @param categoryType the categoryType to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new categoryType, or with status {@code 400 (Bad Request)} if the categoryType has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<?> createCategoryType(@RequestHeader HttpHeaders headers, @Valid @RequestBody CategoryType categoryType)
        throws URISyntaxException {
        log.debug("REST request to save CategoryType : {}", categoryType);
        if (categoryType.getId() != null) {
            throw new BadRequestAlertException("A new categoryType cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CategoryType result = categoryTypeRepository.save(categoryType);
        return new ResponseEntity<>(new BaseResponse<>(true, "Created successfully.", result), HttpStatus.CREATED);
    }

    /**
     * {@code PUT  /category-types/:id} : Updates an existing categoryType.
     *
     * @param id the id of the categoryType to save.
     * @param categoryType the categoryType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryType,
     * or with status {@code 400 (Bad Request)} if the categoryType is not valid,
     * or with status {@code 500 (Internal Server Error)} if the categoryType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCategoryType(
        @RequestHeader HttpHeaders headers,
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody CategoryType categoryType
    ) throws URISyntaxException {
        log.debug("REST request to update CategoryType : {}, {}", id, categoryType);
        if (categoryType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        CategoryType result = categoryTypeRepository.save(categoryType);
        return new ResponseEntity<>(new BaseResponse<>(true, "Update successfully.", result), HttpStatus.OK);
    }

    /**
     * {@code PATCH  /category-types/:id} : Partial updates given fields of an existing categoryType, field will ignore if it is null
     *
     * @param id the id of the categoryType to save.
     * @param categoryType the categoryType to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated categoryType,
     * or with status {@code 400 (Bad Request)} if the categoryType is not valid,
     * or with status {@code 404 (Not Found)} if the categoryType is not found,
     * or with status {@code 500 (Internal Server Error)} if the categoryType couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<?> partialUpdateCategoryType(
        @RequestHeader HttpHeaders headers,
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody CategoryType categoryType
    ) throws URISyntaxException {
        log.debug("REST request to partial update CategoryType partially : {}, {}", id, categoryType);
        if (categoryType.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, categoryType.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!categoryTypeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<CategoryType> result = categoryTypeRepository
            .findById(categoryType.getId())
            .map(existingCategoryType -> {
                if (categoryType.getTitle() != null) {
                    existingCategoryType.setTitle(categoryType.getTitle());
                }
                if (categoryType.getDescription() != null) {
                    existingCategoryType.setDescription(categoryType.getDescription());
                }

                return existingCategoryType;
            })
            .map(categoryTypeRepository::save);

        return new ResponseEntity<>(new BaseResponse<>(true, "Patch successfully.", result), HttpStatus.OK);
        /*return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, categoryType.getId().toString())
        );*/
    }

    /**
     * {@code GET  /category-types} : get all the categoryTypes.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of categoryTypes in body.
     */
    @GetMapping("")
    public ResponseEntity<?> getAllCategoryTypes(@RequestHeader HttpHeaders headers) {
        log.debug("REST request to get all CategoryTypes");
        return new ResponseEntity<>(new BaseResponse<>(true, "Inquiry successfully.", categoryTypeRepository.findAll()), HttpStatus.OK);
    }

    /**
     * {@code GET  /category-types/:id} : get the "id" categoryType.
     *
     * @param id the id of the categoryType to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the categoryType, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getCategoryType(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        log.debug("REST request to get CategoryType : {}", id);
        Optional<CategoryType> categoryType = categoryTypeRepository.findById(id);
        return new ResponseEntity<>(new BaseResponse<>(true, "Inquiry successfully.", categoryType), HttpStatus.OK);
    }

    /**
     * {@code DELETE  /category-types/:id} : delete the "id" categoryType.
     *
     * @param id the id of the categoryType to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategoryType(@RequestHeader HttpHeaders headers, @PathVariable Long id) {
        log.debug("REST request to delete CategoryType : {}", id);
        categoryTypeRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
