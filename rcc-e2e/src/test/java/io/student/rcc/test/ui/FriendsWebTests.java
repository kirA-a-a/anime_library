package io.student.rcc.test.ui;

import static io.student.rcc.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static io.student.rcc.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_INCOME_REQUEST;
import static io.student.rcc.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_OUTCOME_REQUEST;
import static io.student.rcc.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_FRIEND;

import io.student.rcc.jupiter.annotation.DisabledByIssue;
import io.student.rcc.jupiter.extension.UsersQueueExtension.StaticUser;
import io.student.rcc.jupiter.extension.UsersQueueExtension.UserType;
import org.junit.jupiter.api.Test;

public class FriendsWebTests {

  @Test
  @DisabledByIssue("3")
  public void testForAnnotationWithFriend(@UserType(WITH_FRIEND) StaticUser user) {}

  @Test
  public void testForAnnotationWithEmpty(@UserType(EMPTY) StaticUser user) {}

  @Test
  public void testForAnnotationWithWitchIncomeRequest(@UserType(WITH_INCOME_REQUEST) StaticUser user) {}

  @Test
  public void testForAnnotationWithWitchOutcomeRequest(@UserType(WITH_OUTCOME_REQUEST) StaticUser user) {}

}
