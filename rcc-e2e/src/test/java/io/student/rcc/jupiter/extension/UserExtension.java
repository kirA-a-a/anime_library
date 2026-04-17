package io.student.rcc.jupiter.extension;

import static io.student.rcc.utils.RandomUserDataUtils.getRandomFirstName;
import static io.student.rcc.utils.RandomUserDataUtils.getRandomLastName;
import static io.student.rcc.utils.RandomUserDataUtils.getRandomUserName;

import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.UserJson;
import io.student.rcc.service.UsersClient;
import io.student.rcc.service.UsersDbClient;
import java.util.UUID;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

  public static ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);

  private final UsersClient usersClient = new UsersDbClient();
  private final static String DEFAULT_PASSWORD = "12345";

  @Override
  public void beforeEach(ExtensionContext context) {
    AnnotationSupport.findAnnotation(
        context.getRequiredTestMethod(),
        User.class
    ).ifPresent(
        annotation -> {
          UserJson user = new UserJson(
              UUID.randomUUID(),
              getRandomUserName(),
              getRandomFirstName(),
              getRandomLastName(),
              null
          );
          context.getStore(NAMESPACE).put(
              context.getUniqueId(),
              usersClient.createUser(user.username(), DEFAULT_PASSWORD)
          );
        }
    );
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return parameterContext.getParameter().getType().equals(UserJson.class);
  }

  @Override
  public @Nullable Object resolveParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {
    return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(),  UserJson.class);
  }
}
