package io.student.rcc.jupiter.extension;

import io.student.rcc.model.ArtistJson;
import io.student.rcc.model.CountryJson;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.model.PaintingJson;
import io.student.rcc.model.UserJson;
import org.junit.jupiter.api.extension.ExtensionContext;

public record TestData(
    UserJson user,
    CountryJson country,
    ArtistJson artist,
    MuseumJson museum,
    PaintingJson painting
) {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      TestData.class);

  public static TestData empty() {
    return new TestData(null, null, null, null, null);
  }

  public TestData withUser(UserJson user) {
    return new TestData(user, country, artist, museum, painting);
  }

  public TestData withCountry(CountryJson country) {
    return new TestData(user, country, artist, museum, painting);
  }

  public TestData withArtist(ArtistJson artist) {
    return new TestData(user, country, artist, museum, painting);
  }

  public TestData withMuseum(MuseumJson museum) {
    return new TestData(user, country, artist, museum, painting);
  }

  public TestData withPainting(PaintingJson painting) {
    return new TestData(user, country, artist, museum, painting);
  }
}
