package io.student.rcc.test.grpc;

import static io.student.rcc.utils.RandomUserDataUtils.randomBiographyArtist;
import static io.student.rcc.utils.RandomUserDataUtils.randomName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import io.student.rcc.jupiter.annotation.ApiLogin;
import io.student.rcc.jupiter.annotation.Artist;
import io.student.rcc.jupiter.annotation.Token;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.ArtistJson;
import io.student.rococo.grpc.ArtistByIdRequest;
import io.student.rococo.grpc.ArtistsRequest;
import io.student.rococo.grpc.ArtistsResponse;
import org.junit.jupiter.api.Test;

public class ArtistGrpcTest extends BaseGrpcTest {

  @User
  @ApiLogin
  @Test
  void getArtists(@Token String bearerToken) {
    ArtistsResponse response = withAuth(artistStub, bearerToken)
        .getArtists(ArtistsRequest.newBuilder().setPage(0).setSize(10).build());

    assertEquals(10, response.getArtistsCount());
  }

  @User(artists = @Artist(
      name = "Vincent van Grpc",
      biography = "Dutch post-impressionist painter"
  ))
  @ApiLogin
  @Test
  void getArtistById(@Token String bearerToken, ArtistJson artist) {
    io.student.rococo.grpc.Artist response = withAuth(artistStub, bearerToken)
        .getArtistById(ArtistByIdRequest.newBuilder().setId(artist.id().toString()).build());

    assertEquals(artist.id().toString(), response.getId());
    assertEquals(artist.name(), response.getName());
    assertEquals(artist.biography(), response.getBiography());
  }

  @User
  @ApiLogin
  @Test
  void createArtist(@Token String bearerToken) {
    String name = randomName();
    String biography = randomBiographyArtist();

    io.student.rococo.grpc.Artist response = withAuth(artistStub, bearerToken)
        .createArtist(io.student.rococo.grpc.Artist.newBuilder()
            .setName(name)
            .setBiography(biography)
            .build());

    assertFalse(response.getId().isBlank());
    assertEquals(name, response.getName());
    assertEquals(biography, response.getBiography());
  }

  @User(artists = @Artist(
      name = "Old Grpc Artist",
      biography = "Will be updated"
  ))
  @ApiLogin
  @Test
  void updateArtist(@Token String bearerToken, ArtistJson artist) {
    String newName = randomName() + " Grpc";
    String newBiography = randomBiographyArtist();

    io.student.rococo.grpc.Artist response = withAuth(artistStub, bearerToken)
        .updateArtist(io.student.rococo.grpc.Artist.newBuilder()
            .setId(artist.id().toString())
            .setName(newName)
            .setBiography(newBiography)
            .setPhoto(artist.photo() == null ? "" : artist.photo())
            .build());

    assertEquals(artist.id().toString(), response.getId());
    assertEquals(newName, response.getName());
    assertEquals(newBiography, response.getBiography());
    assertNotNull(response.getPhoto());
  }
}
