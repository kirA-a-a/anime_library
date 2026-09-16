package io.student.rcc.jupiter.extension;

import static io.student.rcc.utils.RandomUserDataUtils.randomLastName;
import static io.student.rcc.utils.RandomUserDataUtils.randomName;
import static io.student.rcc.utils.RandomUserDataUtils.randomUserName;

import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.UserJson;
import io.student.rcc.service.UsersClient;
import io.student.rcc.service.UsersDbClient;
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

public class UserExtension implements BeforeEachCallback, AfterEachCallback, ParameterResolver {

  public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(
      UserExtension.class);
  private final UsersClient client = new UsersDbClient();
  private static final String DEFAULT_PASSWORD = "12345";

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        User.class
    ).ifPresent(
        userAnnotation -> {
          String username = userAnnotation.username().isEmpty()
              ? randomUserName()
              : userAnnotation.username();
          UUID id = client.createUser(username, DEFAULT_PASSWORD);
          UserJson user = new UserJson(
              id,
              username,
              randomName(),
              randomLastName(),
              null
          );
          context.getStore(NAMESPACE).put(context.getUniqueId(), user);
          context.getStore(TestData.NAMESPACE).put(
              context.getUniqueId(),
              TestData.empty().withUser(user)
          );
        }
    );
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {
    Optional.ofNullable(context.getStore(NAMESPACE).get(context.getUniqueId(), UserJson.class))
        .ifPresent(client::delete);
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(UserJson.class);
  }

  @Override
  public @Nullable Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
  }
}
