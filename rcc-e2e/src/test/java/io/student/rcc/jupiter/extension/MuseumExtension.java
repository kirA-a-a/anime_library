package io.student.rcc.jupiter.extension;

import static io.student.rcc.utils.RandomUserDataUtils.randomCity;
import static io.student.rcc.utils.RandomUserDataUtils.randomMuseumTitle;
import static io.student.rcc.utils.RandomUserDataUtils.randomSentence;

import io.student.rcc.jupiter.annotation.Museum;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.CountryJson;
import io.student.rcc.model.MuseumJson;
import io.student.rcc.service.MuseumsClient;
import io.student.rcc.service.MuseumsDbClient;
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

public class MuseumExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      MuseumExtension.class);
  private static final String DEFAULT_PHOTO = "images/museum.png";
  private final MuseumsClient client = new MuseumsDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Optional<User> userAnnotation = AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(), User.class);
    if (userAnnotation.isEmpty()) {
      return;
    }

    Optional<Museum> museumAnnotation = Arrays.stream(userAnnotation.get().museums()).findFirst();
    if (museumAnnotation.isEmpty()) {
      return;
    }

    CountryJson country = context.getStore(CountryExtension.NAMESPACE)
        .get(context.getUniqueId(), CountryJson.class);

    MuseumJson museum = client.create(new MuseumJson(
        null,
        museumAnnotation.get().title().isEmpty()
            ? randomMuseumTitle()
            : museumAnnotation.get().title(),
        museumAnnotation.get().description().isEmpty()
            ? randomSentence(8)
            : museumAnnotation.get().description(),
        museumAnnotation.get().city().isEmpty()
            ? randomCity()
            : museumAnnotation.get().city(),
        "data:image/png;base64," + Base64.getEncoder().encodeToString(
            getClass().getClassLoader().getResourceAsStream(
                museumAnnotation.get().content().isEmpty()
                    ? DEFAULT_PHOTO
                    : museumAnnotation.get().content()
            ).readAllBytes()
        ),
        country.id()
    ));
    context.getStore(NAMESPACE).put(context.getUniqueId(), museum);

    TestData testData = context.getStore(TestData.NAMESPACE)
        .get(context.getUniqueId(), TestData.class);
    context.getStore(TestData.NAMESPACE).put(
        context.getUniqueId(),
        testData.withMuseum(museum)
    );
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    Optional.ofNullable(context.getStore(NAMESPACE).get(context.getUniqueId(), MuseumJson.class))
        .ifPresent(client::delete);
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
