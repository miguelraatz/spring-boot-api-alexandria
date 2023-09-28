package com.betrybe.alexandria.controller;

import com.betrybe.alexandria.dto.AuthorDTO;
import com.betrybe.alexandria.dto.BookDTO;
import com.betrybe.alexandria.dto.ResponseDTO;
import com.betrybe.alexandria.models.entities.Author;
import com.betrybe.alexandria.models.entities.Book;
import com.betrybe.alexandria.service.AuthorService;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/authors")
public class AuthorController {

  private final AuthorService authorService;

  @Autowired
  public AuthorController(AuthorService authorService) {
    this.authorService = authorService;
  }

  @PostMapping()
  public ResponseEntity<ResponseDTO<Author>> insertBook(@RequestBody AuthorDTO authorDTO) {
    Author insertedAuthor = authorService.insertAuthor(authorDTO.toAuthor());
    ResponseDTO<Author> responseDTO = new ResponseDTO<>(
        "Author criado com sucesso!", insertedAuthor
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
  }

  @PutMapping("/{authorId}")
  public ResponseEntity<ResponseDTO<Author>> updateAuthor(@PathVariable Long authorId, @RequestBody AuthorDTO authorDTO) {
    Optional<Author> updatedAuthor = authorService.updateAuthor(authorId, authorDTO.toAuthor());
    if (updatedAuthor.isPresent()) {
      ResponseDTO<Author> responseDTO = new ResponseDTO<>(
          String.format("Não foi encontrado o ator de ID %d", authorId), null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
    ResponseDTO<Author> responseDTO = new ResponseDTO<>(
        "Livro atualizado com sucesso!", updatedAuthor.get()
    );
    return ResponseEntity.ok(responseDTO);
  }

  @DeleteMapping("/{authorId}")
  public ResponseEntity<ResponseDTO<Author>> deleteBook(@PathVariable Long authorId) {
    Optional<Author> removedAuthor = authorService.removeAuthorById(authorId);
    if (removedAuthor.isEmpty()) {
      ResponseDTO<Author> responseDTO = new ResponseDTO<>(
          "Ator não encontrado!", null
      );
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
    ResponseDTO<Author> responseDTO = new ResponseDTO<>(
        "Ator removido com sucesso!", null
    );
    return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
  }

  @GetMapping("/{authorId}")
  public ResponseEntity<ResponseDTO<Author>> getBookById(@PathVariable Long authorId) {
    Optional<Author> optionalAuthor = authorService.getAuthorById(authorId);
    if (optionalAuthor.isEmpty()) {
      ResponseDTO<Author> responseDTO = new ResponseDTO<>(
          "Ator não encontrado!", null
      );
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
    ResponseDTO<Author> responseDTO = new ResponseDTO<>(
        "Ator encontrado com sucesso!", optionalAuthor.get()
    );
    return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
  }

  @GetMapping()
  public List<AuthorDTO> getAllAuthors() {
    List<Author> allAuthors = authorService.getAllAuthors();

    return allAuthors.stream()
        .map(author -> new AuthorDTO(
            author.getId(),
            author.getName(),
            author.getNationality(),
            author.getBooks()
        ))
        .collect(Collectors.toList());
  }

}
