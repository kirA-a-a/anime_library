package io.student.rcc.jupiter.extension;

import static io.student.rcc.utils.RandomUserDataUtils.randomSentence;

import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.ArtistJson;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.model.PaintingJson;
import io.student.rcc.service.PaintingsClient;
import io.student.rcc.service.PaintingsDbClient;
import io.student.rcc.utils.RandomUserDataUtils;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class PaintingExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  private final PaintingsClient client = new PaintingsDbClient();
  private static final String DEFAULT_CONTENT_PATH = "images/placeholder.png";
  public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      PaintingExtension.class
  );

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
        .ifPresent(userAnnotation ->
            Arrays.stream(userAnnotation.paintings()).findFirst().ifPresent(paintingAnnotation -> {
              ArtistJson artist = Objects.requireNonNull(
                  context.getStore(ArtistExtension.NAMESPACE)
                      .get(context.getUniqueId(), ArtistJson.class),
                  "Artist is required for PaintingExtension"
              );
              MuseumJson museum = context.getStore(MuseumExtension.NAMESPACE)
                  .get(context.getUniqueId(), MuseumJson.class);

              PaintingJson painting = client.create(new PaintingJson(
                  null,
                  paintingAnnotation.title().isEmpty()
                      ? RandomUserDataUtils.randomPaintingTitle()
                      : paintingAnnotation.title(),
                  paintingAnnotation.description().isEmpty()
                      ? randomSentence(8)
                      : paintingAnnotation.description(),
                  paintingAnnotation.content().isEmpty()
                      ? DEFAULT_CONTENT_PATH
                      : paintingAnnotation.content(),
                  artist.id(),
                  museum != null ? museum.id() : null
              ));
              context.getStore(NAMESPACE).put(context.getUniqueId(), painting);

              TestData testData = context.getStore(TestData.NAMESPACE)
                  .get(context.getUniqueId(), TestData.class);
              context.getStore(TestData.NAMESPACE).put(
                  context.getUniqueId(),
                  testData.withPainting(painting)
              );
            })
        );
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    Optional.ofNullable(context.getStore(NAMESPACE).get(context.getUniqueId(), PaintingJson.class))
        .ifPresent(client::delete);
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(PaintingJson.class);
  }

  @Override
  public @Nullable Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE)
        .get(extensionContext.getUniqueId(), PaintingJson.class);
  }
}
