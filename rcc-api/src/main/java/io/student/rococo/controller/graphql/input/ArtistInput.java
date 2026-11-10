package io.student.rococo.controller.graphql.input;

import io.student.rococo.model.ArtistJson;
import java.util.UUID;

/**
 * GraphQL input ArtistInput → ArtistJson.
 */
public record ArtistInput(UUID id, String name, String biography, String photo) {

  public ArtistJson toJson() {
    return new ArtistJson(id, name, biography, photo);
  }
}
