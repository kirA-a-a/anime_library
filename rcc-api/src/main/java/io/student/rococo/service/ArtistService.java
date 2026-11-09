package io.student.rococo.service;

import io.student.rococo.data.entity.ArtistEntity;
import io.student.rococo.data.repository.ArtistRepository;
import io.student.rococo.model.ArtistJson;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class ArtistService {

  private final ArtistRepository artistRepository;

  @Autowired
  public ArtistService(ArtistRepository artistRepository) {
    this.artistRepository = artistRepository;
  }

  public ArtistJson getArtistById(String id) {
    ArtistEntity entity = artistRepository
        .findByIdSwapped(id)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return new ArtistJson(
        entity.getId(),
        entity.getName(),
        entity.getBiography(),
        Arrays.toString(entity.getPhoto() == null ? null : entity.getPhoto()));
  }

  public Page<ArtistJson> getArtists(Pageable pageable) {
    return artistRepository.findAll(pageable)
        .map(e -> new ArtistJson(
            e.getId(),
            e.getName(),
            e.getBiography(),
            Arrays.toString(e.getPhoto() == null ? null : e.getPhoto())
        ));
  }

  public Page<ArtistJson> getArtistByName(Pageable pageable, String name) {
    return artistRepository.findByNameContainsIgnoreCase(name, pageable)
        .map(e -> new ArtistJson(
            e.getId(),
            e.getName(),
            e.getBiography(),
            Arrays.toString(e.getPhoto() == null ? null : e.getPhoto())
        ));
  }

  public ArtistJson saveArtist(ArtistJson artistJson) {

    ArtistEntity entity = new ArtistEntity();
    entity.setId(artistJson.id());
    entity.setName(artistJson.name());
    entity.setBiography(artistJson.biography());
    entity.setPhoto(artistJson.photo().getBytes());

    ArtistEntity save = artistRepository.save(entity);

    return new ArtistJson(
        save.getId(),
        save.getName(),
        save.getBiography(),
        Arrays.toString(save.getPhoto() == null ? null : save.getPhoto())
    );
  }

  public ArtistJson updateArtist(ArtistJson artistJson) {

    Optional<ArtistEntity> entityOpt = artistRepository.findById(artistJson.id())
        .map(e -> {
          if (artistJson.name() != null) {
            e.setName(artistJson.name());
          }
          if (artistJson.biography() != null) {
            e.setBiography(artistJson.biography());
          }
          if (artistJson.photo() == null) {
            e.setPhoto(null);
          } else {
            e.setPhoto(artistJson.photo().getBytes(StandardCharsets.UTF_8));
          }
          return artistRepository.save(e);
        });

    ArtistEntity entity = entityOpt.orElseThrow(
        () -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    return new ArtistJson(
        entity.getId(),
        entity.getName(),
        entity.getBiography(),
        entity.getPhoto() == null ? null : Arrays.toString(entity.getPhoto())
    );
  }
}
