package com.application.demo.book.repository;

import com.application.demo.book.domain.BookRegistration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BookRegistration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BookRegistrationRepository extends JpaRepository<BookRegistration, Long> {}
