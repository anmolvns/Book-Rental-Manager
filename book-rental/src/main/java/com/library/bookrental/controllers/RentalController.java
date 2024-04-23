package com.library.bookrental.controllers;

import com.library.bookrental.dto.RentalDTO;
import com.library.bookrental.services.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/rentals")
@Slf4j
@Tag(name = "Rent Management", description = "Endpoints for managing rent")
public class RentalController {

    @Autowired
    private RentalService rentalService;

    @Operation(
            summary = "Get all rentals",
            description = "Retrieve all rentals.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RentalDTO.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<RentalDTO>> getAllRentals() {
        try {
            log.info("Request received to get all rentals.");
            List<RentalDTO> rentals = rentalService.getAllRentals();
            log.info("Returning all rentals: {}", rentals);
            return new ResponseEntity<>(rentals, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while fetching all rentals.", e);
            throw e;
        }
    }

    @Operation(
            summary = "Rent a book",
            description = "Rent a book.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RentalDTO.class)
                    ),
                    description = "JSON payload with book rental details."
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Book rented successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RentalDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - Book not available for rental.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<RentalDTO> rentBook(@RequestBody RentalDTO rentalDTO) {
        try {
            log.info("Request received to rent book: {}", rentalDTO);
            RentalDTO rentedBook = rentalService.rentBook(rentalDTO);
            log.info("Book rented: {}", rentedBook);
            return new ResponseEntity<>(rentedBook, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("An error occurred while renting the book.", e);
            throw e;
        }
    }

    @Operation(
            summary = "Return a book",
            description = "Return a rented book by providing its ID.",
            parameters = {
                    @Parameter(name = "rentalId", description = "Rental ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Book returned successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = RentalDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Not Found - Rental ID not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class)
                            )
                    )
            }
    )
    @PutMapping("/{rentalId}/return")
    public ResponseEntity<RentalDTO> returnBook(@PathVariable Long rentalId) {
        try {
            log.info("Request received to return book with rental ID: {}", rentalId);
            RentalDTO returnedBook = rentalService.returnBook(rentalId);
            log.info("Book returned: {}", returnedBook);
            return new ResponseEntity<>(returnedBook, HttpStatus.OK);
        } catch (Exception e) {
            log.error("An error occurred while returning the book.", e);
            throw e;
        }
    }
}
