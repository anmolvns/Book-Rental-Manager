package com.library.bookrental.services;

import com.library.bookrental.dto.RentalDTO;
import com.library.bookrental.exceptions.RentalNotFoundException;
import com.library.bookrental.models.Rental;
import com.library.bookrental.repositories.BookRepository;
import com.library.bookrental.repositories.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Slf4j
public class RentalService {

    @Autowired
    private RentalRepository rentalRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<RentalDTO> getAllRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        return rentals.stream()
                .map(RentalDTO::new).toList();
    }

    public RentalDTO rentBook(RentalDTO rentalDTO) {
        try {
            Rental rental = new Rental();
            rental.setBook(rentalDTO.getBook());
            rental.setRenterName(rentalDTO.getRenterName());
            rental.setRentalDate(LocalDate.now());

            Rental savedRental = rentalRepository.save(rental);
            return new RentalDTO(savedRental);
        } catch (Exception e) {
            log.error("Error occurred while renting the book.", e);
            throw e;
        }
    }

    public RentalDTO returnBook(Long rentalId) {
        try {
            Rental rental = rentalRepository.findById(rentalId)
                    .orElseThrow(() -> new RentalNotFoundException("Rental not found with ID: " + rentalId));

            rental.setReturnDate(LocalDate.now());
            Rental savedRental = rentalRepository.save(rental);

            return new RentalDTO(savedRental);
        } catch (RentalNotFoundException e) {
            log.warn("Rental not found while returning book: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An error occurred while returning the book.", e);
            throw e;
        }
    }
}
