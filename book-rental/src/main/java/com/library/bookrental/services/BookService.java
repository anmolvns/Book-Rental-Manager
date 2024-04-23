package com.library.bookrental.services;

import com.library.bookrental.dto.BookDTO;
import com.library.bookrental.exceptions.AuthorNotFoundException;
import com.library.bookrental.exceptions.BookNotFoundException;
import com.library.bookrental.exceptions.RentalNotFoundException;
import com.library.bookrental.models.Author;
import com.library.bookrental.models.Book;
import com.library.bookrental.models.Rental;
import com.library.bookrental.repositories.AuthorRepository;
import com.library.bookrental.repositories.BookRepository;
import com.library.bookrental.repositories.RentalRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class BookService {

    private static final int RENTAL_PERIOD_DAYS = 14;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private RentalRepository rentalRepository;

    public List<BookDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream().map(BookDTO::new).toList();
    }

    public BookDTO addBook(BookDTO bookDTO) {
        Author author = authorRepository.findById(bookDTO.getAuthorId())
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with ID: " + bookDTO.getAuthorId()));

        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setIsbn(bookDTO.getIsbn());
        book.setPublicationYear(bookDTO.getPublicationYear());
        book.setAuthor(author);

        Book savedBook = bookRepository.save(book);
        log.info("Book added: {}", savedBook);
        return new BookDTO(savedBook);
    }

    public BookDTO updateBook(Long id, BookDTO bookDTO) {
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            book.setTitle(bookDTO.getTitle());
            book.setIsbn(bookDTO.getIsbn());
            book.setPublicationYear(bookDTO.getPublicationYear());

            Author author = authorRepository.findById(bookDTO.getAuthorId())
                    .orElseThrow(() -> new AuthorNotFoundException("Author not found with ID: " + bookDTO.getAuthorId()));

            book.setAuthor(author);

            Book updatedBook = bookRepository.save(book);
            log.info("Book updated: {}", updatedBook);
            return new BookDTO(updatedBook);
        } else {
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
    }

    public void deleteBook(Long id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new BookNotFoundException("Book not found with ID: " + id);
        }
        log.info("Book deleted with ID: {}", id);
    }

    public List<BookDTO> getBooksByAuthor(Long authorId) {
        Author author = authorRepository.findById(authorId)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with ID: " + authorId));

        List<Book> books = bookRepository.findByAuthor(author);
        return books.stream().map(BookDTO::new).toList();
    }

    public List<BookDTO> getBooksAvailableForRent() {
        List<Book> availableBooks = bookRepository.findByAvailableTrue();
        return availableBooks.stream().map(BookDTO::new).toList();
    }

    public List<BookDTO> getBooksCurrentlyRented() {
        List<Book> rentedBooks = bookRepository.findByAvailableFalse();
        return rentedBooks.stream().map(BookDTO::new).toList();
    }

    public void rentBook(Long bookId, String renterName) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));

        if (!book.isAvailable()) {
            throw new RuntimeException("Book is already rented.");
        }

        Rental rental = new Rental();
        rental.setBook(book);
        rental.setRenterName(renterName);
        rental.setRentalDate(LocalDate.now());

        rentalRepository.save(rental);

        book.setAvailable(false);
        bookRepository.save(book);
    }

    public void returnBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new BookNotFoundException("Book not found with ID: " + bookId));

        Rental rental = (Rental) rentalRepository.findFirstByBookBookIdAndReturnDateIsNull(bookId)
                .orElseThrow(() -> new RentalNotFoundException("No active rental found for book with ID: " + bookId));

        rental.setReturnDate(LocalDate.now());
        rentalRepository.save(rental);

        book.setAvailable(true);
        bookRepository.save(book);
    }

    public void checkForOverdueRentals() {
        List<Rental> rentals = rentalRepository.findAll();
        LocalDate currentDate = LocalDate.now();

        for (Rental rental : rentals) {
            LocalDate rentalDate = rental.getRentalDate();
            LocalDate dueDate = rentalDate.plusDays(RENTAL_PERIOD_DAYS);

            if (currentDate.isAfter(dueDate)) {
                // Mark the rental as overdue
                rental.setOverdue(true);
                rentalRepository.save(rental);

            }
        }
    }

}
