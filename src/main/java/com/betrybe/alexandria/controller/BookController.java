package com.betrybe.alexandria.controller;

import com.betrybe.alexandria.dto.BookDTO;
import com.betrybe.alexandria.dto.BookDetailDTO;
import com.betrybe.alexandria.dto.ResponseDTO;
import com.betrybe.alexandria.models.entities.Book;
import com.betrybe.alexandria.models.entities.BookDetail;
import com.betrybe.alexandria.service.BookService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books")
public class BookController {

  private final BookService bookService;

  @Autowired
  public BookController(BookService bookService) {
    this.bookService = bookService;
  }

  @PostMapping()
  public ResponseEntity<ResponseDTO<Book>> insertBook(@RequestBody BookDTO bookDTO) {
    Book insertedBook = bookService.insertBook(bookDTO.toBook());
    ResponseDTO<Book> responseDTO = new ResponseDTO<>(
        "Book inserted successfully", insertedBook
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
  }

  @PutMapping("/{bookId}")
  public ResponseEntity<ResponseDTO<Book>> updateBook(
      @PathVariable Long bookId,
      @RequestBody BookDTO bookDTO
  ) {
    Optional<Book> updatedBook = bookService.updateBook(bookId, bookDTO.toBook());
    if (updatedBook.isPresent()) {
      ResponseDTO<Book> responseDTO = new ResponseDTO<>(
          String.format("Não foi encontrado o livro de ID %d", bookId), null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
    ResponseDTO<Book> responseDTO = new ResponseDTO<>(
        "Livro atualizado com sucesso!", updatedBook.get()
    );
    return ResponseEntity.ok(responseDTO);
  }

  @DeleteMapping("/{bookId}")
  public ResponseEntity<ResponseDTO<Book>> deleteBook(@PathVariable Long bookId) {
    Optional<Book> removedBook = bookService.removeBookById(bookId);
    if (removedBook.isEmpty()) {
      ResponseDTO<Book> responseDTO = new ResponseDTO<>(
          "Livro não encontrado!", null
      );
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
    ResponseDTO<Book> responseDTO = new ResponseDTO<>(
        "Livro removido com sucesso!", null
    );
    return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
  }

  @GetMapping("/{bookId}")
  public ResponseEntity<ResponseDTO<Book>> getBookById(@PathVariable Long bookId) {
    Optional<Book> optionalBook = bookService.getBookById(bookId);
    if (optionalBook.isEmpty()) {
      ResponseDTO<Book> responseDTO = new ResponseDTO<>(
          "Livro não encontrado!", null
      );
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
    ResponseDTO<Book> responseDTO = new ResponseDTO<>(
      "Livro encontrado com sucesso!", optionalBook.get()
    );
    return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
  }

  @GetMapping()
  public List<BookDTO> getAllBooks() {
    List<Book> allBooks = bookService.getAllBooks();

    return allBooks.stream()
        .map(book -> new BookDTO(
            book.getId(),
            book.getTitle(),
            book.getGenre(),
            book.getDetails()
        ))
        .collect(Collectors.toList());
  }

  @PostMapping("/{bookId}/details/")
  public ResponseEntity<ResponseDTO<BookDetail>> insertBookDetail(
      @RequestBody BookDetailDTO bookDetailDTO,
      @PathVariable Long bookId
  ) {
    Optional<BookDetail> insertedBookDetail = bookService.insertBookDetail(
        bookId,
        bookDetailDTO.toBookDetail()
    );
    ResponseDTO<BookDetail> responseDTO = new ResponseDTO<>(
        "Book details inserted successfully", insertedBookDetail.get()
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
  }

  @PutMapping("/{bookId}/details/{id}")
  public ResponseEntity<ResponseDTO<BookDetail>> updateBookDetail(
      @PathVariable Long bookDetailId,
      @RequestBody BookDetailDTO bookDetailDTO
  ) {
    Optional<BookDetail> updatedBookDetail = bookService.updateBookDetail(
        bookDetailId,
        bookDetailDTO.toBookDetail()
    );
    if (updatedBookDetail.isPresent()) {
      ResponseDTO<BookDetail> responseDTO = new ResponseDTO<>(
          String.format(
              "Não foi encontrado os detalhes do livro de ID %d",
              bookDetailId
          ), null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
    ResponseDTO<BookDetail> responseDTO = new ResponseDTO<>(
        "Detalhes do livro atualizado com sucesso!", updatedBookDetail.get()
    );
    return ResponseEntity.ok(responseDTO);
  }

  @DeleteMapping("/{bookId}/details/{id}")
  public ResponseEntity<ResponseDTO<BookDetail>> deleteBookDetail(@PathVariable Long id) {
    Optional<BookDetail> removedBookDetail = bookService.removeBookDetailById(id);
    if (removedBookDetail.isEmpty()) {
      ResponseDTO<BookDetail> responseDTO = new ResponseDTO<>(
          "Detalhes do livro não encontrado!", null
      );
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
    ResponseDTO<BookDetail> responseDTO = new ResponseDTO<>(
        "Detalhes do livro removido com sucesso!", null
    );
    return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
  }

  @GetMapping("/{bookId}/details/{id}")
  public ResponseEntity<ResponseDTO<BookDetail>> getBookDetailById(@PathVariable Long id) {
    Optional<BookDetail> optionalBookDetail = bookService.getBookDetailById(id);
    if (optionalBookDetail.isEmpty()) {
      ResponseDTO<BookDetail> responseDTO = new ResponseDTO<>(
          "Detalhes do livro não encontrado!", null
      );
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
    ResponseDTO<BookDetail> responseDTO = new ResponseDTO<>(
        "Detalhes do livro encontrado com sucesso!", optionalBookDetail.get()
    );
    return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
  }

  @PutMapping("/{bookId}/publisher/{publisherId}")
  public ResponseEntity<ResponseDTO<Book>> setPublisherFromBook(
      @PathVariable Long bookId,
      @PathVariable Long publisherId
  ) {
    Optional<Book> optionalBook = bookService.setPublisher(bookId, publisherId);

    if(optionalBook.isEmpty()) {
      ResponseDTO<Book> responseDTO = new ResponseDTO<>(
          String.format("Não foi encontrado o livro de ID %d ou a editora de ID %d",
              bookId, publisherId), null
      );
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }

    ResponseDTO<Book> responseDTO = new ResponseDTO<>(
        "Editora vinculada ao livro com sucesso!", optionalBook.get());
    return ResponseEntity.ok(responseDTO);
  }

  @DeleteMapping("/{bookId}/publisher")
  public ResponseEntity<ResponseDTO<Book>> removePublisherFromBook(@PathVariable Long bookId) {
    Optional<Book> optionalBook = bookService.removePublisher(bookId);
    if(optionalBook.isEmpty()){
      ResponseDTO<Book> responseDTO = new ResponseDTO<>(
          String.format("Não foi possível remover a editora do livro com id %d", bookId),
          null
      );

      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
    }

    ResponseDTO<Book> responseDTO = new ResponseDTO<>(
        String.format("Editora removida do livro de ID %d", bookId),
        optionalBook.get()
    );

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(responseDTO);
  }
}
