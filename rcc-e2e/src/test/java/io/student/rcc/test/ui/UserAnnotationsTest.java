package io.student.rcc.test.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.student.rcc.jupiter.annotation.Artist;
import io.student.rcc.jupiter.annotation.Country;
import io.student.rcc.jupiter.annotation.Museum;
import io.student.rcc.jupiter.annotation.Painting;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.ArtistJson;
import io.student.rcc.model.CountryJson;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.model.PaintingJson;
import io.student.rcc.model.UserJson;
import io.student.rcc.service.ArtistsClient;
import io.student.rcc.service.ArtistsDbClient;
import io.student.rcc.service.CountriesClient;
import io.student.rcc.service.CountriesDbClient;
import io.student.rcc.service.MuseumsClient;
import io.student.rcc.service.MuseumsDbClient;
import io.student.rcc.service.PaintingsClient;
import io.student.rcc.service.PaintingsDbClient;
import org.junit.jupiter.api.Test;

public class UserAnnotationsTest {

  private final ArtistsClient artistsClient = new ArtistsDbClient();
  private final CountriesClient countriesClient = new CountriesDbClient();
  private final MuseumsClient museumsClient = new MuseumsDbClient();
  private final PaintingsClient paintingsClient = new PaintingsDbClient();

  @Test
  @User
  void userShouldBeCreatedWhenUserAnnotationIsPresent(UserJson user) {
    assertNotNull(user);
    assertNotNull(user.id());
    assertFalse(user.username().isBlank());
  }

  @Test
  @User(
      artists = @Artist(
          name = "e2e_artist_from_annotation",
          biography = "Dutch post-impressionist"
      )
  )
  void artistShouldBeCreatedWithValuesFromAnnotation(ArtistJson artist) {
    assertEquals("e2e_artist_from_annotation", artist.name());
    assertEquals("Dutch post-impressionist", artist.biography());

    ArtistJson fromDb = artistsClient.findById(artist.id()).orElseThrow();
    assertEquals(artist.name(), fromDb.name());
    assertEquals(artist.biography(), fromDb.biography());
  }

  @Test
  @User(
      countries = @Country(name = "e2e_country_from_annotation")
  )
  void countryShouldBeCreatedWithValuesFromAnnotation(CountryJson country) {
    assertEquals("e2e_country_from_annotation", country.name());

    CountryJson fromDb = countriesClient.findByName(country.name()).orElseThrow();
    assertEquals(country.id(), fromDb.id());
  }

  @Test
  @User(
      countries = @Country(name = "e2e_country_for_museum"),
      museums = @Museum(
          title = "e2e_museum_from_annotation",
          description = "Museum in Paris",
          city = "Paris"
      )
  )
  void museumShouldBeLinkedToCountryFromUserAnnotation(CountryJson country, MuseumJson museum) {
    assertEquals("e2e_museum_from_annotation", museum.title());
    assertEquals("Museum in Paris", museum.description());
    assertEquals("Paris", museum.city());
    assertEquals(country.id(), museum.countryId());

    MuseumJson fromDb = museumsClient.findById(museum.id()).orElseThrow();
    assertEquals(country.id(), fromDb.countryId());
  }

  @Test
  @User(
      artists = @Artist(name = "e2e_artist_for_painting"),
      paintings = @Painting(
          title = "e2e_starry_night",
          description = "1889 oil on canvas"
      )
  )
  void paintingShouldBeCreatedWithoutMuseumWhenMuseumNotSpecified(
      ArtistJson artist,
      PaintingJson painting
  ) {
    assertEquals("e2e_starry_night", painting.title());
    assertEquals("1889 oil on canvas", painting.description());
    assertEquals(artist.id(), painting.artistId());
    assertNull(painting.museumId());

    PaintingJson fromDb = paintingsClient.findById(painting.id()).orElseThrow();
    assertEquals(artist.id(), fromDb.artistId());
    assertNull(fromDb.museumId());
  }

  @Test
  @User(
      countries = @Country(name = "e2e_country_full_graph"),
      artists = @Artist(
          name = "e2e_artist_full_graph",
          biography = "Full graph artist"
      ),
      museums = @Museum(
          title = "e2e_museum_full_graph",
          description = "Full graph museum",
          city = "Amsterdam"
      ),
      paintings = @Painting(
          title = "e2e_painting_full_graph",
          description = "Full graph painting"
      )
  )
  void paintingShouldBeLinkedToArtistAndMuseumFromUserAnnotation(
      UserJson user,
      CountryJson country,
      ArtistJson artist,
      MuseumJson museum,
      PaintingJson painting
  ) {
    assertNotNull(user.id());
    assertEquals(country.id(), museum.countryId());
    assertEquals(artist.id(), painting.artistId());
    assertEquals(museum.id(), painting.museumId());
    assertEquals("e2e_painting_full_graph", painting.title());
  }

  @Test
  @User(
      artists = {
          @Artist(name = "e2e_first_artist_only"),
          @Artist(name = "e2e_second_artist_ignored")
      }
  )
  void onlyFirstArtistShouldBeCreatedWhenMultipleArtistsSpecified(ArtistJson artist) {
    assertEquals("e2e_first_artist_only", artist.name());
    assertTrue(artistsClient.findByName("e2e_first_artist_only").isPresent());
    assertTrue(artistsClient.findByName("e2e_second_artist_ignored").isEmpty());
  }

  @Test
  @User(
      artists = @Artist,
      paintings = @Painting
  )
  void randomValuesShouldBeGeneratedWhenAnnotationFieldsAreEmpty(
      ArtistJson artist,
      PaintingJson painting
  ) {
    assertFalse(artist.name().isBlank());
    assertFalse(artist.biography().isBlank());
    assertFalse(painting.title().isBlank());
    assertFalse(painting.description().isBlank());
    assertEquals(artist.id(), painting.artistId());
  }
}
