package io.student.rcc.jupiter.extension;

import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.ArtistJson;
import io.student.rcc.service.ArtistsClient;
import io.student.rcc.service.ArtistsDbClient;
import io.student.rcc.utils.RandomUserDataUtils;
import java.util.Arrays;
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

public class ArtistExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  private final ArtistsClient client = new ArtistsDbClient();
  public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      ArtistExtension.class
  );

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {

    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        User.class
    ).ifPresent(
        userAnnotation ->
        {
          Arrays.stream(userAnnotation.artists()).findFirst().ifPresent(
              artistAnnotation -> {
                UUID id = UUID.randomUUID();
                ArtistJson artist = new ArtistJson(
                    id,
                    artistAnnotation.name().isEmpty() ? RandomUserDataUtils.randomArtistName()
                        : artistAnnotation.name(),
                    artistAnnotation.biography().isEmpty() ? RandomUserDataUtils.randomSentence(8)
                        : artistAnnotation.biography(),
                    RandomUserDataUtils.randomSentence(10)
                );
                context
                    .getStore(NAMESPACE)
                    .put(
                        context.getUniqueId(),
                        client.create(artist)
                    );
              }
          );
        }
    );

  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    Optional.ofNullable(context.getStore(NAMESPACE).get(context.getUniqueId(), ArtistJson.class))
        .ifPresent(
            client::delete
        );
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(ArtistJson.class);
  }

  @Override
  public @Nullable Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE)
        .get(extensionContext.getUniqueId(), ArtistJson.class);
  }
}
