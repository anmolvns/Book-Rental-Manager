package com.library.bookrental.dto;

import com.library.bookrental.models.Book;
import com.library.bookrental.models.Rental;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
public class RentalDTO {
    private Long rentalId;
    private Book book;
    private String renterName;
    private LocalDate rentalDate;
    private LocalDate returnDate;
    private boolean overdue;

    public RentalDTO(Rental rental){
        this.rentalId = rental.getRentalId();
        this.book = rental.getBook();
        this.renterName = rental.getRenterName();
        this.rentalDate = rental.getRentalDate();
        this.returnDate = rental.getReturnDate();
        this.overdue = rental.isOverdue();
    }
}
