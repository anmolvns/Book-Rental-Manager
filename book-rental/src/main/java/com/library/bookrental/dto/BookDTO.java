package com.library.bookrental.dto;

import com.library.bookrental.models.Book;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BookDTO {
    private Long bookId;
    private String title;
    private String isbn;
    private int publicationYear;
    private Long authorId;

    private boolean available;

    public BookDTO(Book book) {
        this.bookId = book.getBookId();
        this.title = book.getTitle();
        this.isbn = book.getIsbn();
        this.publicationYear = book.getPublicationYear();
        this.authorId = book.getAuthor().getAuthorId();
        this.available = book.isAvailable();
    }
}