package io.student.rococo.controller.graphql.input;

import io.student.rococo.model.MuseumJson;
import java.util.UUID;

/**
 * GraphQL input MuseumInput → MuseumJson.
 */
public record MuseumInput(
    UUID id,
    String title,
    String description,
    String photo,
    GeoInput geo
) {

  public MuseumJson toJson() {
    return new MuseumJson(
        id,
        title,
        description,
        photo,
        geo == null ? null : geo.toJson()
    );
  }
}
