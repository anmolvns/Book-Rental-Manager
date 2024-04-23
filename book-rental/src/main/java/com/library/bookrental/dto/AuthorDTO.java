package com.library.bookrental.dto;

import com.library.bookrental.models.Author;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AuthorDTO {
    private Long authorId;
    private String name;
    private String biography;

    public AuthorDTO(Author author){
        this.authorId = author.getAuthorId();
        this.name = author.getName();
        this.biography = author.getBiography();
    }
}
