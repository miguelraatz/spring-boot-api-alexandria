package com.betrybe.alexandria.dto;

import com.betrybe.alexandria.models.entities.Author;
import com.betrybe.alexandria.models.entities.Book;
import java.util.List;

public record AuthorDTO(Long id, String name, String nationality, List<Book> books) {
  public Author toAuthor() {
    return new Author(id, name, nationality, books);
  }
}
