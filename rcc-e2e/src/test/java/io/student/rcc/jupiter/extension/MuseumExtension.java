package io.student.rcc.jupiter.extension;

import static io.student.rcc.utils.RandomUserDataUtils.randomCity;
import static io.student.rcc.utils.RandomUserDataUtils.randomMuseumTitle;
import static io.student.rcc.utils.RandomUserDataUtils.randomSentence;

import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.CountryJson;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.model.PaintingJson;
import io.student.rcc.service.MuseumsClient;
import io.student.rcc.service.MuseumsDbClient;
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

public class MuseumExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
      .create(MuseumExtension.class);
  private final MuseumsClient client = new MuseumsDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {

    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        User.class
    ).ifPresent(
        userAnnotation -> {

          Arrays.stream(userAnnotation.museums()).findFirst().ifPresent(
              museumAnnotation -> {
                UUID id = UUID.randomUUID();
                MuseumJson museum = new MuseumJson(
                    id,
                    museumAnnotation.title().isEmpty() ? randomMuseumTitle()
                        : museumAnnotation.title(),
                    museumAnnotation.description().isEmpty() ? randomSentence(8)
                        : museumAnnotation.description(),
                    museumAnnotation.city().isEmpty() ? randomCity() : museumAnnotation.city(),
                    randomSentence(8),
                    context.getStore(CountryExtension.NAMESPACE)
                        .get(context.getUniqueId(), CountryJson.class).id()
                );

                context.getStore(NAMESPACE).put(context.getUniqueId(), client.create(museum));
              }
          );
        }
    );

  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    Optional.ofNullable(context.getStore(NAMESPACE).get(context.getUniqueId(), MuseumJson.class))
        .ifPresent(
            client::delete
        );
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(MuseumJson.class);
  }

  @Override
  public @Nullable Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE)
        .get(extensionContext.getUniqueId(), MuseumJson.class);
  }
}
