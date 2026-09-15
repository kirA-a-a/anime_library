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

}
