package com.library.bookrental.repositories;

import com.library.bookrental.models.Author;
import com.library.bookrental.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {

    List<Book> findByAvailableTrue();

    List<Book> findByAuthor(Author author);

    List<Book> findByAvailableFalse();
}
