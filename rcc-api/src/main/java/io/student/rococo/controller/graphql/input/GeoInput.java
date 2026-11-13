package io.student.rococo.controller.graphql.input;

import io.student.rococo.model.GeoJson;

/**
 * GraphQL input GeoInput → GeoJson.
 */
public record GeoInput(String city, CountryInput country) {

  public GeoJson toJson() {
    return new GeoJson(
        city,
        country == null ? null : country.toJson()
    );
  }
}
