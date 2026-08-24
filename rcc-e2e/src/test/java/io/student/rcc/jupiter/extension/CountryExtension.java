package io.student.rcc.jupiter.extension;

import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.CountryJson;
import io.student.rcc.service.CountriesClient;
import io.student.rcc.service.CountriesDbClient;
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

public class CountryExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace
      .create(CountryExtension.class);
  private final CountriesClient client = new CountriesDbClient();

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {

    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        User.class
    ).ifPresent(
        userAnnotation -> {
          Arrays.stream(userAnnotation.countries()).findFirst().ifPresent(
              countryAnnotation -> {
                UUID id = UUID.randomUUID();
                CountryJson country = new CountryJson(
                    id,
                    countryAnnotation.name().isEmpty() ? RandomUserDataUtils.randomName()
                        : countryAnnotation.name()
                );
                context.getStore(NAMESPACE).put(context.getUniqueId(), client.create(country));
              }
          );

        }
    );

  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    Optional.ofNullable(context.getStore(NAMESPACE).get(context.getUniqueId(), CountryJson.class))
        .ifPresent(client::delete);
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(CountryJson.class);

  }

  @Override
  public @Nullable Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE)
        .get(extensionContext.getUniqueId(), CountryJson.class);
  }
}
