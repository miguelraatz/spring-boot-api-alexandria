package com.betrybe.alexandria.dto;

import com.betrybe.alexandria.models.entities.Book;
import com.betrybe.alexandria.models.entities.BookDetail;
import com.fasterxml.jackson.annotation.JsonProperty;

public record BookDetailDTO(
    Long id,
    String summary,
    @JsonProperty("page_count")
    Integer pageCount,
    String year,
    String isbn,
    Book book
) {
    public BookDetail toBookDetail() {
        return new BookDetail(id, summary, pageCount, year, isbn, book);
    }
}
