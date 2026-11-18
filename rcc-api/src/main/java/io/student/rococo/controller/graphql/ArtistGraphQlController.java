package io.student.rococo.controller.graphql;

import io.student.rococo.controller.graphql.input.ArtistInput;
import io.student.rococo.model.ArtistJson;
import io.student.rococo.service.ArtistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * GraphQL Query/Mutation для художников — аналог {@code ArtistController}.
 */
@Controller
public class ArtistGraphQlController {

  private final ArtistService artistService;

  @Autowired
  public ArtistGraphQlController(ArtistService artistService) {
    this.artistService = artistService;
  }

  @QueryMapping
  public Slice<ArtistJson> artists(@Argument String name,
                                   @Argument int page,
                                   @Argument int size) {
    Pageable pageable = PageRequest.of(page, size);
    if (name == null || name.isBlank()) {
      return artistService.getArtists(pageable);
    }
    return artistService.getArtistByName(pageable, name);
  }

  @QueryMapping
  public ArtistJson artist(@Argument String id) {
    return artistService.getArtistById(id);
  }

  @MutationMapping
  public ArtistJson addArtist(@Argument ArtistInput artist) {
    return artistService.saveArtist(artist.toJson());
  }

  @MutationMapping
  public ArtistJson updateArtist(@Argument ArtistInput artist) {
    return artistService.updateArtist(artist.toJson());
  }
}
