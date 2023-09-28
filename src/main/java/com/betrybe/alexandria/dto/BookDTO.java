package com.betrybe.alexandria.dto;

import com.betrybe.alexandria.models.entities.Book;
import com.betrybe.alexandria.models.entities.BookDetail;

public record BookDTO(
    Long id,
    String title,
    String genre,
    BookDetail details
) {
  public Book toBook() {
    return new Book(id, title, genre, details);
  }
}
