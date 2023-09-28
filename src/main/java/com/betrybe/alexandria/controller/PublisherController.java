package com.betrybe.alexandria.controller;

import com.betrybe.alexandria.dto.BookDTO;
import com.betrybe.alexandria.dto.PublisherDTO;
import com.betrybe.alexandria.dto.ResponseDTO;
import com.betrybe.alexandria.models.entities.Book;
import com.betrybe.alexandria.models.entities.Publisher;
import com.betrybe.alexandria.service.PublisherService;
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
@RequestMapping("/publishers")
public class PublisherController {

  private final PublisherService publisherService;

  @Autowired

  public PublisherController(PublisherService publisherService) {
    this.publisherService = publisherService;
  }

  @PostMapping()
  public ResponseEntity<ResponseDTO<Publisher>> insertPublisher(@RequestBody PublisherDTO publisherDTO) {
    Publisher insertedPublisher = publisherService.insertPublisher(publisherDTO.toPublisher());
    ResponseDTO<Publisher> responseDTO = new ResponseDTO<>(
        "Publisher inserted successfully", insertedPublisher
    );
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
  }

  @PutMapping("/{publisherId}")
  public ResponseEntity<ResponseDTO<Publisher>> updatePublisher(@PathVariable Long publisherId, @RequestBody PublisherDTO publisherDTO) {
    Optional<Publisher> updatedPublisher = publisherService.updatePublisher(publisherId, publisherDTO.toPublisher());
    if (updatedPublisher.isPresent()) {
      ResponseDTO<Publisher> responseDTO = new ResponseDTO<>(
          String.format("Não foi encontrado a editora de ID %d", publisherId), null);
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
    ResponseDTO<Publisher> responseDTO = new ResponseDTO<>(
        "Editora atualizada com sucesso!", updatedPublisher.get()
    );
    return ResponseEntity.ok(responseDTO);
  }

  @DeleteMapping("/{publisherId}")
  public ResponseEntity<ResponseDTO<Publisher>> deletePublisher(@PathVariable Long publisherId) {
    Optional<Publisher> removedPublisher = publisherService.removePublisherById(publisherId);
    if (removedPublisher.isEmpty()) {
      ResponseDTO<Publisher> responseDTO = new ResponseDTO<>(
          "Editora não encontrada!", null
      );
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
    ResponseDTO<Publisher> responseDTO = new ResponseDTO<>(
        "Editora removida com sucesso!", null
    );
    return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
  }

  @GetMapping("/{publisherId}")
  public ResponseEntity<ResponseDTO<Publisher>> getPublisherById(@PathVariable Long publisherId) {
    Optional<Publisher> optionalPublisher = publisherService.getPublisherById(publisherId);
    if (optionalPublisher.isEmpty()) {
      ResponseDTO<Publisher> responseDTO = new ResponseDTO<>(
          "Editora não encontrada!", null
      );
      return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseDTO);
    }
    ResponseDTO<Publisher> responseDTO = new ResponseDTO<>(
        "Editora encontrada com sucesso!", optionalPublisher.get()
    );
    return ResponseEntity.status(HttpStatus.OK).body(responseDTO);
  }

  @GetMapping()
  public List<PublisherDTO> getAllPublishers() {
    List<Publisher> allPublishers = publisherService.getAllPublishers();

    return allPublishers.stream()
        .map(publisher -> new PublisherDTO(
            publisher.getId(),
            publisher.getName(),
            publisher.getAddress(),
            publisher.getBooks()
        ))
        .collect(Collectors.toList());
  }
}
