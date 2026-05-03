package io.student.rcc.jupiter.extension;

import io.qameta.allure.Allure;
import io.student.rcc.jupiter.extension.UsersQueueExtension.UserType.Type;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Parameter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ExtensionContext.Namespace;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

@SuppressWarnings("unchecked")
public class UsersQueueExtension implements
    BeforeEachCallback,
    AfterEachCallback,
    ParameterResolver {

  public static final Namespace NAMESPACE = Namespace.create(UsersQueueExtension.class);

  public record StaticUser(
      String username, String password, String friend, String income, String outcome
  ) {

  }

  public static final Queue<StaticUser> EMPTY_USERS = new ConcurrentLinkedDeque<>();
  public static final Queue<StaticUser> WITH_FRIEND_USERS = new ConcurrentLinkedDeque<>();
  public static final Queue<StaticUser> WITCH_INCOME_REQUEST_USERS = new ConcurrentLinkedDeque<>();
  public static final Queue<StaticUser> WITCH_OUTCOME_REQUEST_USERS = new ConcurrentLinkedDeque<>();

  static {
    EMPTY_USERS.add(new StaticUser("Ivan", "12345", null, null, null));
    WITH_FRIEND_USERS.add(new StaticUser("Ivan2", "12345", "Ivan", null, null));
    WITCH_INCOME_REQUEST_USERS.add(new StaticUser("admin", "12345", null, null, null));
    WITCH_OUTCOME_REQUEST_USERS.add(new StaticUser("poopa", "12345", null, null, null));
  }

  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.PARAMETER)
  @ExtendWith({UsersQueueExtension.class})
  public @interface UserType {

    Type value() default Type.EMPTY;

    enum Type {
      EMPTY, WITH_FRIEND, WITCH_INCOME_REQUEST, WITCH_OUTCOME_REQUEST
    }

  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    Parameter[] parameters = context.getRequiredTestMethod().getParameters();

    IntStream.range(0, parameters.length)
        .filter(i -> AnnotationSupport.isAnnotated(parameters[i], UserType.class))
        .forEach(i -> {
          Parameter parameter = parameters[i];
          UserType annotation = parameter.getAnnotation(UserType.class);
          Optional<StaticUser> user = Optional.empty();
          StopWatch stopWatch = StopWatch.createStarted();

          while (user.isEmpty() && stopWatch.getTime(TimeUnit.SECONDS) < 30) {

            user = switch (annotation.value()) {
              case EMPTY -> Optional.ofNullable(EMPTY_USERS.poll());
              case WITH_FRIEND -> Optional.ofNullable(WITH_FRIEND_USERS.poll());
              case WITCH_INCOME_REQUEST -> Optional.ofNullable(WITCH_INCOME_REQUEST_USERS.poll());
              case WITCH_OUTCOME_REQUEST -> Optional.ofNullable(WITCH_OUTCOME_REQUEST_USERS.poll());
            };
          }

          Allure.getLifecycle().updateTestCase(testCase -> testCase.setStart(new Date().getTime()));

          user.ifPresentOrElse(u ->
                  ((Map<Integer, StaticUser>) context.getStore(NAMESPACE)
                      .computeIfAbsent(context.getUniqueId(), key -> new HashMap()))
                      .put(i, u),

              () -> {
                throw new IllegalStateException("Can't find user after 30 sec");
              });
        });
  }

  @Override
  public void afterEach(ExtensionContext context) throws Exception {

    Map<Integer, StaticUser> map = context.getStore(NAMESPACE)
        .get(context.getUniqueId(), Map.class);

    Parameter[] parameters = context.getRequiredTestMethod().getParameters();

    for (Entry<Integer, StaticUser> entry : map.entrySet()) {

      int i = entry.getKey();
      StaticUser u = entry.getValue();
      UserType ut = parameters[i].getAnnotation(UserType.class);

      switch (ut.value()) {
        case Type.EMPTY -> EMPTY_USERS.add(u);
        case Type.WITH_FRIEND -> WITH_FRIEND_USERS.add(u);
        case Type.WITCH_INCOME_REQUEST -> WITCH_INCOME_REQUEST_USERS.add(u);
        case Type.WITCH_OUTCOME_REQUEST -> WITCH_OUTCOME_REQUEST_USERS.add(u);
      }

    }
  }

  @Override
  public boolean supportsParameter(ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {

    return
        parameterContext.getParameter().getType().equals(StaticUser.class)
            &&
            AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
  }

  @Override
  public Object resolveParameter(
      ParameterContext parameterContext,
      ExtensionContext extensionContext) throws ParameterResolutionException {

    Map<Integer, StaticUser> users =
        (Map<Integer, StaticUser>) extensionContext.getStore(NAMESPACE)
            .get(extensionContext.getUniqueId());

    int index = parameterContext.getIndex();

    return users.get(index);
  }
}
