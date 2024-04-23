package com.library.bookrental.repositories;

import com.library.bookrental.models.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RentalRepository extends JpaRepository<Rental, Long> {
    Optional<Object> findFirstByBookBookIdAndReturnDateIsNull(Long bookId);
}
