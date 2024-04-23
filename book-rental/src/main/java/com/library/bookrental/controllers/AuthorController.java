package com.library.bookrental.controllers;

import com.library.bookrental.dto.AuthorDTO;
import com.library.bookrental.services.AuthorService;
import com.library.bookrental.exceptions.AuthorNotFoundException;
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
@RequestMapping("/api/authors")
@Slf4j
@Tag(name = "Author Management", description = "Endpoints for managing authors")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @Operation(
            summary = "Get all authors",
            description = "Retrieve all authors.",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Successful operation",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthorDTO.class)
                            )
                    )
            }
    )
    @GetMapping
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        log.info("Request received to get all authors.");
        List<AuthorDTO> authors = authorService.getAllAuthors();
        log.info("Returning all authors: {}", authors);
        return new ResponseEntity<>(authors, HttpStatus.OK);
    }

    @Operation(
            summary = "Add an author",
            description = "Add a new author.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDTO.class)
                    ),
                    description = "JSON payload with author details."
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Author added successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthorDTO.class)
                            )
                    )
            }
    )
    @PostMapping
    public ResponseEntity<AuthorDTO> addAuthor(@RequestBody AuthorDTO authorDTO) {
        log.info("Request received to add author: {}", authorDTO);
        AuthorDTO addedAuthor = authorService.addAuthor(authorDTO);
        log.info("Author added: {}", addedAuthor);
        return new ResponseEntity<>(addedAuthor, HttpStatus.CREATED);
    }

    @Operation(
            summary = "Update an author",
            description = "Update an existing author by ID.",
            parameters = {
                    @Parameter(name = "id", description = "Author ID", required = true)
            },
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AuthorDTO.class)
                    ),
                    description = "JSON payload with updated author details."
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Author updated successfully",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = AuthorDTO.class)
                            )
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Author not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class)
                            )
                    )
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody AuthorDTO authorDTO) {
        try {
            log.info("Request received to update author with ID {}: {}", id, authorDTO);
            AuthorDTO updatedAuthor = authorService.updateAuthor(id, authorDTO);
            log.info("Author updated: {}", updatedAuthor);
            return new ResponseEntity<>(updatedAuthor, HttpStatus.OK);
        } catch (AuthorNotFoundException e) {
            log.error("Author not found while updating author: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("An error occurred while updating the author.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(
            summary = "Delete an author",
            description = "Delete an existing author by ID.",
            parameters = {
                    @Parameter(name = "id", description = "Author ID", required = true)
            },
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Author deleted successfully"
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Author not found.",
                            content = @Content(
                                    mediaType = "application/json",
                                    schema = @Schema(implementation = Map.class)
                            )
                    )
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAuthor(@PathVariable Long id) {
        try {
            log.info("Request received to delete author with ID: {}", id);
            authorService.deleteAuthor(id);
            log.info("Author deleted with ID: {}", id);
            return new ResponseEntity<>("Author deleted successfully.", HttpStatus.OK);
        } catch (AuthorNotFoundException e) {
            log.warn("Author not found with id: {}", id);
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("An error occurred while deleting the author.", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
