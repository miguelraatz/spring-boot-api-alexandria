package com.betrybe.alexandria.service;

import com.betrybe.alexandria.models.entities.Author;
import com.betrybe.alexandria.models.entities.Publisher;
import com.betrybe.alexandria.models.repositories.PublisherRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PublisherService {

  private final PublisherRepository publisherRepository;

  @Autowired
  public PublisherService(PublisherRepository publisherRepository) {
    this.publisherRepository = publisherRepository;
  }

  public Publisher insertPublisher(Publisher publisher) {
    return publisherRepository.save(publisher);
  }

  public Optional<Publisher> updatePublisher(Long id, Publisher publisher) {
    Optional<Publisher> optionalPublisher = publisherRepository.findById(id);

    if (optionalPublisher.isPresent()) {
      Publisher publisherFromDb = optionalPublisher.get();
      publisherFromDb.setName(publisher.getName());
      publisherFromDb.setAddress(publisher.getAddress());

      return Optional.of(publisherRepository.save(publisherFromDb));
    }
    return optionalPublisher;
  }

  public Optional<Publisher> removePublisherById(Long id) {
    Optional<Publisher> optionalPublisher = publisherRepository.findById(id);

    if (optionalPublisher.isPresent()) {
      publisherRepository.deleteById(id);
    }

    return optionalPublisher;
  }

  public Optional<Publisher> getPublisherById(Long id) {
    return publisherRepository.findById(id);
  }

  public List<Publisher> getAllPublishers() {
    return publisherRepository.findAll();
  }
}
