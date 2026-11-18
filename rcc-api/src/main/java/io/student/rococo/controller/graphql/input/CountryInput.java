package io.student.rococo.controller.graphql.input;

import io.student.rococo.model.CountryJson;
import java.util.UUID;

/**
 * GraphQL input CountryInput → CountryJson.
 */
public record CountryInput(UUID id, String name) {

  public CountryJson toJson() {
    return new CountryJson(id, name);
  }
}
