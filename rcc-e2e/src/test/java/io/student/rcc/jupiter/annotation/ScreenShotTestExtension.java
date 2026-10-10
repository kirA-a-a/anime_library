package io.student.rcc.jupiter.annotation;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Allure;
import io.student.rcc.jupiter.extension.TestMethodContextExtension;
import io.student.rcc.model.allure.ScreenDif;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import javax.imageio.ImageIO;
import lombok.SneakyThrows;
import org.jspecify.annotations.Nullable;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.platform.commons.support.AnnotationSupport;
import org.springframework.core.io.ClassPathResource;

public class ScreenShotTestExtension implements ParameterResolver, TestExecutionExceptionHandler {

  public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ScreenShotTestExtension.class);
  public static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  @Override
  public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
    return AnnotationSupport.isAnnotated(extensionContext.getRequiredTestMethod(), ScreenShotTest.class)
        && parameterContext.getParameter().getType().isAssignableFrom(BufferedImage.class);
  }

  @SneakyThrows
  @Override
  public @Nullable BufferedImage resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {

    ScreenShotTest screenShotTest = AnnotationSupport
        .findAnnotation(
            extensionContext.getRequiredTestMethod(),
            ScreenShotTest.class
        )
        .orElseThrow();

    BufferedImage expected = ImageIO.read(new ClassPathResource(screenShotTest.value()).getInputStream());

    setExpected(expected);
    return expected;
  }

  @Override
  public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {

    ScreenShotTest screenShotTest = context.getRequiredTestMethod().getAnnotation(ScreenShotTest.class);

    if (screenShotTest != null && screenShotTest.rewriteExpected()) {
      BufferedImage actual = getActual();
      if (actual != null) {
        // build/resources/test/... - копия ожидаемого в classpath; тест читает expected отсюда
        File classpathFile = new ClassPathResource(screenShotTest.value()).getFile();
        ImageIO.write(actual, "png", classpathFile);

        // src/test/resources/... - исходник ожидаемого; без этой записи gradle на следующем
        // прогоне снова скопирует старый expected из src поверх build
        File srcFile = new File("src/test/resources/" + screenShotTest.value());
        ImageIO.write(actual, "png", srcFile);
      }
    }

    BufferedImage expected = getExpected();
    BufferedImage actual = getActual();
    BufferedImage dif = getDif();

    if (expected != null && actual != null && dif != null) {
      ScreenDif screenDif = new ScreenDif(
          "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(expected)),
          "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(actual)),
          "data:image/png;base64," + Base64.getEncoder().encodeToString(imageToBytes(dif))
      );

      Allure.addAttachment(
          "Screenshot diff",
          "application/vnd.allure.image.diff",
          OBJECT_MAPPER.writeValueAsString(screenDif)
      );
    }

    throw throwable;
  }

  public static void setExpected(BufferedImage expected) {
    TestMethodContextExtension.context().getStore(NAMESPACE).put("expected", expected);
  }

  public static BufferedImage getExpected() {
    return TestMethodContextExtension.context().getStore(NAMESPACE).get("expected", BufferedImage.class);
  }

  public static void setActual(BufferedImage actual) {
    TestMethodContextExtension.context().getStore(NAMESPACE).put("actual", actual);
  }

  public static BufferedImage getActual() {
    return TestMethodContextExtension.context().getStore(NAMESPACE).get("actual", BufferedImage.class);
  }

  public static void setDif(BufferedImage dif) {
    TestMethodContextExtension.context().getStore(NAMESPACE).put("dif", dif);
  }

  public static BufferedImage getDif() {
    return TestMethodContextExtension.context().getStore(NAMESPACE).get("dif", BufferedImage.class);
  }

  private static byte[] imageToBytes(BufferedImage image) {
    try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
      ImageIO.write(image, "png", byteArrayOutputStream);
      return byteArrayOutputStream.toByteArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

}
