package io.student.rcc.jupiter.extension;

import io.student.rcc.model.ArtistJson;
import io.student.rcc.model.CountryJson;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.model.PaintingJson;
import io.student.rcc.model.UserJson;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import org.junit.jupiter.api.extension.ExtensionContext;

@ParametersAreNonnullByDefault
public record TestData(
    @Nullable UserJson user,
    @Nullable CountryJson country,
    @Nullable ArtistJson artist,
    @Nullable MuseumJson museum,
    @Nullable PaintingJson painting
) {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      TestData.class);

  public static @Nonnull TestData empty() {
    return new TestData(null, null, null, null, null);
  }

  public @Nonnull TestData withUser(UserJson user) {
    return new TestData(user, country, artist, museum, painting);
  }

  public @Nonnull TestData withCountry(CountryJson country) {
    return new TestData(user, country, artist, museum, painting);
  }

  public @Nonnull TestData withArtist(ArtistJson artist) {
    return new TestData(user, country, artist, museum, painting);
  }

  public @Nonnull TestData withMuseum(MuseumJson museum) {
    return new TestData(user, country, artist, museum, painting);
  }

  public @Nonnull TestData withPainting(PaintingJson painting) {
    return new TestData(user, country, artist, museum, painting);
  }
}
