package io.student.rcc.test.grpc;

import static io.student.rcc.utils.RandomUserDataUtils.randomCity;
import static io.student.rcc.utils.RandomUserDataUtils.randomMuseumTitle;
import static io.student.rcc.utils.RandomUserDataUtils.randomSentence;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.student.rcc.jupiter.annotation.ApiLogin;
import io.student.rcc.jupiter.annotation.Country;
import io.student.rcc.jupiter.annotation.Museum;
import io.student.rcc.jupiter.annotation.Token;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.CountryJson;
import io.student.rcc.model.MuseumJson;
import io.student.rococo.grpc.Geo;
import io.student.rococo.grpc.MuseumByIdRequest;
import io.student.rococo.grpc.MuseumsRequest;
import io.student.rococo.grpc.MuseumsResponse;
import org.junit.jupiter.api.Test;

public class MuseumGrpcTest extends BaseGrpcTest {

  @User
  @ApiLogin
  @Test
  void getMuseums(@Token String bearerToken) {
    MuseumsResponse response = withAuth(museumStub, bearerToken)
        .getMuseums(MuseumsRequest.newBuilder().setPage(0).setSize(10).build());

    assertEquals(10, response.getMuseumsCount());
  }

  @User(
      countries = @Country(name = "grpc_country_for_museum_get"),
      museums = @Museum(
          title = "grpc_museum_by_id",
          description = "Museum for getById",
          city = "Paris"
      )
  )
  @ApiLogin
  @Test
  void getMuseumById(@Token String bearerToken, MuseumJson museum) {
    io.student.rococo.grpc.Museum response = withAuth(museumStub, bearerToken)
        .getMuseumById(MuseumByIdRequest.newBuilder().setId(museum.id().toString()).build());

    assertEquals(museum.id().toString(), response.getId());
    assertEquals(museum.title(), response.getTitle());
    assertEquals(museum.description(), response.getDescription());
    assertEquals(museum.city(), response.getGeo().getCity());
  }

  @User(countries = @Country(name = "grpc_country_for_museum_create"))
  @ApiLogin
  @Test
  void createMuseum(@Token String bearerToken, CountryJson country) {
    String title = randomMuseumTitle();
    String description = randomSentence(8);
    String city = randomCity();

    io.student.rococo.grpc.Museum response = withAuth(museumStub, bearerToken)
        .createMuseum(io.student.rococo.grpc.Museum.newBuilder()
            .setTitle(title)
            .setDescription(description)
            .setPhoto("")
            .setGeo(Geo.newBuilder()
                .setCity(city)
                .setCountry(io.student.rococo.grpc.Country.newBuilder()
                    .setId(country.id().toString())
                    .setName(country.name())
                    .build())
                .build())
            .build());

    assertFalse(response.getId().isBlank());
    assertEquals(title, response.getTitle());
    assertEquals(description, response.getDescription());
    assertEquals(city, response.getGeo().getCity());
    assertEquals(country.id().toString(), response.getGeo().getCountry().getId());
  }

  @User(
      countries = @Country(name = "grpc_country_for_museum_update"),
      museums = @Museum(
          title = "Old Grpc Museum",
          description = "Will be updated",
          city = "Rome"
      )
  )
  @ApiLogin
  @Test
  void updateMuseum(@Token String bearerToken, CountryJson country, MuseumJson museum) {
    String newTitle = randomMuseumTitle() + " Grpc";
    String newDescription = randomSentence(8);
    String newCity = randomCity();

    io.student.rococo.grpc.Museum response = withAuth(museumStub, bearerToken)
        .updateMuseum(io.student.rococo.grpc.Museum.newBuilder()
            .setId(museum.id().toString())
            .setTitle(newTitle)
            .setDescription(newDescription)
            .setPhoto(museum.photo() == null ? "" : museum.photo())
            .setGeo(Geo.newBuilder()
                .setCity(newCity)
                .setCountry(io.student.rococo.grpc.Country.newBuilder()
                    .setId(country.id().toString())
                    .setName(country.name())
                    .build())
                .build())
            .build());

    assertEquals(museum.id().toString(), response.getId());
    assertEquals(newTitle, response.getTitle());
    assertEquals(newDescription, response.getDescription());
    assertEquals(newCity, response.getGeo().getCity());
  }
}
