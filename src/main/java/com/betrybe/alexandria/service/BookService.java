package com.betrybe.alexandria.service;

import com.betrybe.alexandria.models.entities.Book;
import com.betrybe.alexandria.models.entities.BookDetail;
import com.betrybe.alexandria.models.entities.Publisher;
import com.betrybe.alexandria.models.repositories.BookDetailRepository;
import com.betrybe.alexandria.models.repositories.BookRepository;
import com.betrybe.alexandria.models.repositories.PublisherRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  private final BookRepository bookRepository;
  private final BookDetailRepository bookDetailRepository;
  private final PublisherRepository publisherRepository;

  @Autowired
  public BookService(
      BookRepository bookRepository,
      BookDetailRepository bookDetailRepository,
      PublisherRepository publisherRepository
  ) {
    this.bookRepository = bookRepository;
    this.bookDetailRepository = bookDetailRepository;
    this.publisherRepository = publisherRepository;
  }
  public Book insertBook(Book book) {
    return bookRepository.save(book);
  }

  public Optional<Book> updateBook(Long id, Book book) {
    Optional<Book> optionalBook = bookRepository.findById(id);

    if (optionalBook.isPresent()) {
      Book bookFromDb = optionalBook.get();
      bookFromDb.setTitle(book.getTitle());
      bookFromDb.setGenre(book.getGenre());

      return Optional.of(bookRepository.save(bookFromDb));
    }
    return optionalBook;
  }

  public Optional<Book> removeBookById(Long id) {
    Optional<Book> optionalBook = bookRepository.findById(id);

    if (optionalBook.isPresent()) {
      bookRepository.deleteById(id);
    }

    return optionalBook;
  }

  public Optional<Book> getBookById(Long id) {
    return bookRepository.findById(id);
  }

  public List<Book> getAllBooks() {
    return bookRepository.findAll();
  }

  public Optional<BookDetail> insertBookDetail(Long bookId, BookDetail bookDetail) {
    Optional<Book> optionalBook = bookRepository.findById(bookId);

    if (optionalBook.isPresent()) {
      Book book = optionalBook.get();
      bookDetail.setBook(book);
      BookDetail newBookDetail = bookDetailRepository.save(bookDetail);
      return Optional.of(newBookDetail);
    }
    return Optional.empty();
  }

  public Optional<BookDetail> updateBookDetail(Long bookId, BookDetail bookDetail) {
    Optional<Book> optionalBook = bookRepository.findById(bookId);

    if (optionalBook.isPresent()) {
      Book book = optionalBook.get();
      BookDetail bookDetailFromDb = book.getDetails();
      bookDetailFromDb.setSummary(bookDetail.getSummary());
      bookDetailFromDb.setPageCount(bookDetail.getPageCount());
      bookDetailFromDb.setYear(bookDetail.getYear());
      bookDetailFromDb.setIsbn(bookDetail.getIsbn());

      BookDetail updatedBookDetail = bookDetailRepository.save(bookDetailFromDb);
      return Optional.of(bookDetailRepository.save(updatedBookDetail));
    }
    return Optional.empty();
  }

  public Optional<BookDetail> removeBookDetailById(Long bookId) {
    Optional<Book> optionalBook = bookRepository.findById(bookId);

    if (optionalBook.isPresent()) {
      Book book = optionalBook.get();
      Optional<BookDetail> optionalBookDetail = bookDetailRepository.findById(book.getDetails().getId());

      if (optionalBookDetail.isPresent()) {
        book.setDetails(null);
        BookDetail bookDetail = optionalBookDetail.get();
        bookDetailRepository.deleteById(bookDetail.getId());

        return Optional.of(bookDetail);
      }
    }

    return Optional.empty();
  }

  public Optional<BookDetail> getBookDetailById(Long id) {
    return bookDetailRepository.findById(id);
  }

  public Optional<Book> setPublisher(Long bookId, Long publisherId) {
    Optional<Book> optionalBook = bookRepository.findById(bookId);
    if(optionalBook.isEmpty()){
      return Optional.empty();
    }

    Optional<Publisher> optionalPublisher = publisherRepository.findById(publisherId);
    if(optionalPublisher.isEmpty()){
      return Optional.empty();
    }

    Book book = optionalBook.get();
    Publisher publisher = optionalPublisher.get();

    book.setPublisher(publisher);
    Book updatedBook = bookRepository.save(book);

    return Optional.of(updatedBook);

  }

  public Optional<Book> removePublisher(Long bookId) {
    Optional<Book> optionalBook = bookRepository.findById(bookId);
    if(optionalBook.isEmpty()){
      return Optional.empty();
    }

    Book book = optionalBook.get();
    book.setPublisher(null);

    Book newBook = bookRepository.save(book);
    return Optional.of(newBook);
  }
}
