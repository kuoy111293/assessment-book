package com.application.demo.book.repository;

import com.application.demo.book.domain.CategoryType;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the CategoryType entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CategoryTypeRepository extends JpaRepository<CategoryType, Long> {}
