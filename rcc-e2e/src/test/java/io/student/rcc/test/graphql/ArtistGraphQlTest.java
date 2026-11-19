package io.student.rcc.test.graphql;

import static io.student.rcc.utils.RandomUserDataUtils.randomBiographyArtist;
import static io.student.rcc.utils.RandomUserDataUtils.randomName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.apollographql.apollo.api.Mutation;
import com.apollographql.apollo.api.Query;
import com.apollographql.java.rx2.Rx2Apollo;
import io.student.rcc.AddArtistMutation;
import io.student.rcc.ArtistQuery;
import io.student.rcc.ArtistsQuery;
import io.student.rcc.UpdateArtistMutation;
import io.student.rcc.jupiter.annotation.ApiLogin;
import io.student.rcc.jupiter.annotation.Artist;
import io.student.rcc.jupiter.annotation.Token;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.ArtistJson;
import io.student.rcc.type.ArtistInput;
import org.junit.jupiter.api.Test;

public class ArtistGraphQlTest extends BaseGraphQlTest {

  @User
  @Test
  @ApiLogin
  void getAllArtistGraphQl(@Token String bearerToken) {
    ArtistsQuery.Data data = execute(
        ArtistsQuery.builder().page(0).size(10).build(),
        bearerToken
    );

    assertEquals(10, data.artists.edges.size());
  }

  @User(artists = @Artist(
      name = "Vincent van GraphQl",
      biography = "Dutch post-impressionist painter"
  ))
  @Test
  @ApiLogin
  void getArtistByIdGraphQl(@Token String bearerToken, ArtistJson artist) {
    ArtistQuery.Data data = execute(
        ArtistQuery.builder().id(artist.id().toString()).build(),
        bearerToken
    );

    assertEquals(artist.name(), data.artist.name);
    assertEquals(artist.biography(), data.artist.biography);
  }

  @User
  @Test
  @ApiLogin
  void addArtistGraphQl(@Token String bearerToken) {
    String name = randomName();
    String biography = randomBiographyArtist();

    AddArtistMutation.Data data = execute(
        AddArtistMutation.builder()
            .artist(ArtistInput.builder().name(name).biography(biography).build())
            .build(),
        bearerToken
    );

    assertNotNull(data.addArtist.id);
    assertEquals(name, data.addArtist.name);
    assertEquals(biography, data.addArtist.biography);
  }

  @User(artists = @Artist(
      name = "Old GraphQl Artist",
      biography = "Will be updated"
  ))
  @Test
  @ApiLogin
  void updateArtistGraphQl(@Token String bearerToken, ArtistJson artist) {
    String newName = randomName() + " GraphQl";
    String newBiography = randomBiographyArtist();

    UpdateArtistMutation.Data data = execute(
        UpdateArtistMutation.builder()
            .artist(ArtistInput.builder()
                .id(artist.id().toString())
                .name(newName)
                .biography(newBiography)
                .build())
            .build(),
        bearerToken
    );

    assertEquals(artist.id().toString(), data.updateArtist.id);
    assertEquals(newName, data.updateArtist.name);
    assertEquals(newBiography, data.updateArtist.biography);
  }

  private <D extends Query.Data> D execute(Query<D> query, String bearerToken) {
    return Rx2Apollo.single(
            apolloClient.query(query).addHttpHeader("Authorization", bearerToken))
        .blockingGet()
        .dataOrThrow();
  }

  private <D extends Mutation.Data> D execute(Mutation<D> mutation, String bearerToken) {
    return Rx2Apollo.single(
            apolloClient.mutation(mutation).addHttpHeader("Authorization", bearerToken))
        .blockingGet()
        .dataOrThrow();
  }
}
