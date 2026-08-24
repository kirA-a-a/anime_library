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
import java.util.UUID;
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
  public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      PaintingExtension.class
  );

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        User.class
    ).ifPresent(
        userAnnotation -> {

          Arrays.stream(userAnnotation.paintings()).findFirst().ifPresent(
              paintingAnnotation -> {

                UUID id = UUID.randomUUID();

                ArtistJson artistJson = Objects.requireNonNull(
                    context.getStore(ArtistExtension.NAMESPACE)
                        .get(context.getUniqueId(), ArtistJson.class),
                    "Object ArtistJson in PaintingExtension.beforeEach(ExtensionContext context) is requireNonNull");

                MuseumJson museumJson = context.getStore(MuseumExtension.NAMESPACE)
                    .get(context.getUniqueId(), MuseumJson.class);

                PaintingJson painting = new PaintingJson(
                    id,
                    paintingAnnotation.title().isEmpty() ? RandomUserDataUtils.randomPaintingTitle()
                        : paintingAnnotation.title(),
                    paintingAnnotation.description().isEmpty() ? randomSentence(8)
                        : paintingAnnotation.description(),
                    paintingAnnotation.content().isEmpty() ? randomSentence(8)
                        : paintingAnnotation.content(),
                    artistJson.id(),
                    museumJson != null ? museumJson.id() : null
                );

                context.getStore(NAMESPACE).put(context.getUniqueId(), client.create(painting));
              }
          );
        }
    );
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {

    Optional.ofNullable(context.getStore(NAMESPACE).get(context.getUniqueId(), PaintingJson.class))
        .ifPresent(
            client::delete
        );

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
