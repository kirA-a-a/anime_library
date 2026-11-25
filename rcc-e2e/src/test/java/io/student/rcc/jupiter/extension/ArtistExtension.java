package io.student.rcc.jupiter.extension;

import io.student.rcc.jupiter.annotation.Artist;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.ArtistJson;
import io.student.rcc.service.ArtistsClient;
import io.student.rcc.service.ArtistsDbClient;
import io.student.rcc.utils.RandomUserDataUtils;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;
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
  private static final String DEFAULT_IMAGE_PATH = "images/placeholder.png";
  public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      ArtistExtension.class
  );

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Optional<User> userAnnotation = AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(), User.class);
    if (userAnnotation.isEmpty()) {
      return;
    }

    Optional<Artist> artistAnnotation = Arrays.stream(userAnnotation.get().artists()).findFirst();
    if (artistAnnotation.isEmpty()) {
      return;
    }

    ArtistJson artist = client.create(new ArtistJson(
        null,
        artistAnnotation.get().name().isEmpty()
            ? RandomUserDataUtils.randomArtistName()
            : artistAnnotation.get().name(),
        artistAnnotation.get().biography().isEmpty()
            ? RandomUserDataUtils.randomSentence(8)
            : artistAnnotation.get().biography(),
        "data:image/png;base64," + Base64.getEncoder().encodeToString(
            getClass().getClassLoader().getResourceAsStream(DEFAULT_IMAGE_PATH).readAllBytes()
        )
    ));
    context.getStore(NAMESPACE).put(context.getUniqueId(), artist);

    TestData testData = context.getStore(TestData.NAMESPACE)
        .get(context.getUniqueId(), TestData.class);
    context.getStore(TestData.NAMESPACE).put(
        context.getUniqueId(),
        testData.withArtist(artist)
    );
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    Optional.ofNullable(context.getStore(NAMESPACE).get(context.getUniqueId(), ArtistJson.class))
        .ifPresent(client::delete);
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
