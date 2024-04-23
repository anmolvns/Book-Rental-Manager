package com.library.bookrental.services;

import com.library.bookrental.dto.AuthorDTO;
import com.library.bookrental.exceptions.AuthorNotFoundException;
import com.library.bookrental.models.Author;
import com.library.bookrental.repositories.AuthorRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public List<AuthorDTO> getAllAuthors() {
        List<Author> authors = authorRepository.findAll();
        return authors.stream()
                .map(AuthorDTO::new)
                .toList();
    }

    public AuthorDTO addAuthor(AuthorDTO authorDTO) {
        try {
            Author author = new Author();
            author.setName(authorDTO.getName());
            author.setBiography(authorDTO.getBiography());
            Author savedAuthor = authorRepository.save(author);
            return new AuthorDTO(savedAuthor);
        } catch (Exception e) {
            log.error("An error occurred while adding the author: {}", e.getMessage());
            throw e;
        }
    }

    public AuthorDTO updateAuthor(Long id, AuthorDTO authorDTO) {
        try {
            Author author = authorRepository.findById(id)
                    .orElseThrow(() -> new AuthorNotFoundException("Author not found with ID: " + id));
            author.setName(authorDTO.getName());
            author.setBiography(authorDTO.getBiography());
            Author updatedAuthor = authorRepository.save(author);
            return new AuthorDTO(updatedAuthor);
        } catch (AuthorNotFoundException e) {
            log.error("Author not found while updating author: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An error occurred while updating the author: {}", e.getMessage());
            throw e;
        }
    }

    public void deleteAuthor(Long id) {
        try {
            Author author = authorRepository.findById(id)
                    .orElseThrow(() -> new AuthorNotFoundException("Author not found with ID: " + id));
            authorRepository.delete(author);
        } catch (AuthorNotFoundException e) {
            log.error("Author not found while deleting author: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("An error occurred while deleting the author: {}", e.getMessage());
            throw e;
        }
    }
}
