package com.library.bookrental.controllers;

import com.library.bookrental.dto.BookDTO;
import com.library.bookrental.exceptions.AuthorNotFoundException;
import com.library.bookrental.exceptions.BookNotFoundException;
import com.library.bookrental.services.BookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
@Tag(name = "Book Management", description = "Endpoints for managing books")
@Slf4j
public class BookController {

    @Autowired
    private BookService bookService;

    @Operation(
            summary = "Get all books",
            description = "Retrieves a list of all books available in the library.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of books retrieved successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = List.class, example = "[{book1},{book2}]")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error - Unable to retrieve books.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<BookDTO>> getAllBooks() {
        log.info("Request received to get all books.");
        List<BookDTO> books = bookService.getAllBooks();
        log.info("Returning all books: {}", books);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Operation(
            summary = "Add a new book",
            description = "Adds a new book to the library.",
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Book added successfully."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Author not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error - Unable to add the book.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<String> addBook(@RequestBody BookDTO bookDTO) {
        try {
            log.info("Request received to add book: {}", bookDTO);
            bookService.addBook(bookDTO);
            log.info("Book added: {}", bookDTO);
            return new ResponseEntity<>("Book added successfully.", HttpStatus.CREATED);
        } catch (AuthorNotFoundException e) {
            log.error("Author not found while adding book: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("An error occurred while adding the book.", e);
            return new ResponseEntity<>("An error occurred while adding the book.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Update a book",
            description = "Updates an existing book in the library.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book updated successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = BookDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Author not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error - Unable to update the book.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
        try {
            log.info("Request received to update book with ID {}: {}", id, bookDTO);
            BookDTO updatedBook = bookService.updateBook(id, bookDTO);
            log.info("Book updated: {}", updatedBook);
            return new ResponseEntity<>(updatedBook, HttpStatus.OK);
        } catch (AuthorNotFoundException e) {
            log.error("Author not found while updating book: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("An error occurred while updating the book.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Delete a book",
            description = "Deletes a book from the library.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book deleted successfully."
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Book not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error - Unable to delete the book.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        try {
            log.info("Request received to delete book with ID: {}", id);
            bookService.deleteBook(id);
            log.info("Book deleted with ID: {}", id);
            return new ResponseEntity<>("Book deleted successfully.", HttpStatus.OK);
        } catch (BookNotFoundException e) {
            log.warn("Book not found with id: {}", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("An error occurred while deleting the book.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Get books by author",
            description = "Retrieves a list of books written by a specific author.",
            parameters = {
                    @Parameter(name = "authorId", description = "Author ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of books retrieved successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = List.class, example = "[{book1},{book2}]")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error - Unable to retrieve books.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class)
                            )
                    )
            }
    )
    @GetMapping("/byAuthor/{authorId}")
    public ResponseEntity<List<BookDTO>> getBooksByAuthor(@PathVariable Long authorId) {
        log.info("Request received to get books by author with ID: {}", authorId);
        List<BookDTO> books = bookService.getBooksByAuthor(authorId);
        log.info("Returning books by author with ID {}: {}", authorId, books);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Operation(
            summary = "Get books available for rent",
            description = "Retrieves a list of books available for rent.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of books available for rent retrieved successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = List.class, example = "[{book1},{book2}]")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error - Unable to retrieve books.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class)
                            )
                    )
            }
    )
    @GetMapping("/availableForRent")
    public ResponseEntity<List<BookDTO>> getBooksAvailableForRent() {
        log.info("Request received to get books available for rent.");
        List<BookDTO> books = bookService.getBooksAvailableForRent();
        log.info("Returning books available for rent: {}", books);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Operation(
            summary = "Get books currently rented",
            description = "Retrieves a list of books currently rented.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "List of books currently rented retrieved successfully.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = List.class, example = "[{book1},{book2}]")
                            )
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error - Unable to retrieve books.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class)
                            )
                    )
            }
    )
    @GetMapping("/currentlyRented")
    public ResponseEntity<List<BookDTO>> getBooksCurrentlyRented() {
        log.info("Request received to get books currently rented.");
        List<BookDTO> books = bookService.getBooksCurrentlyRented();
        log.info("Returning books currently rented: {}", books);
        return new ResponseEntity<>(books, HttpStatus.OK);
    }

    @Operation(
            summary = "Rent a book",
            description = "Rent a specific book by providing the book ID and the renter's name.",
            parameters = {
                    @Parameter(name = "bookId", description = "Book ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book rented successfully."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error - Unable to rent the book.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )
    @PostMapping("/{bookId}/rent")
    public ResponseEntity<?> rentBook(@PathVariable Long bookId, @RequestParam String renterName) {
        try {
            bookService.rentBook(bookId, renterName);
            return ResponseEntity.ok("Book rented successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error renting the book: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Return a book",
            description = "Return a specific book by providing the book ID.",
            parameters = {
                    @Parameter(name = "bookId", description = "Book ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book returned successfully."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error - Unable to return the book.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )
    @PostMapping("/{bookId}/return")
    public ResponseEntity<?> returnBook(@PathVariable Long bookId) {
        try {
            bookService.returnBook(bookId);
            return ResponseEntity.ok("Book returned successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error returning the book: " + e.getMessage());
        }
    }

    @Operation(
            summary = "Check for overdue rentals",
            description = "Check for overdue rentals and take necessary actions.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Overdue rentals checked successfully."
                    ),
                    @ApiResponse(
                            responseCode = "500",
                            description = "Internal Server Error - Unable to check for overdue rentals.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = String.class)
                            )
                    )
            }
    )
    @GetMapping("/overdue")
    public ResponseEntity<?> checkForOverdueRentals() {
        try {
            bookService.checkForOverdueRentals();
            return ResponseEntity.ok("Overdue rentals checked successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error checking for overdue rentals: " + e.getMessage());
        }
    }

}
