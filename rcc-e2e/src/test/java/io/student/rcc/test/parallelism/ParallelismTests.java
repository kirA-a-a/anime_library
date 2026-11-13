package io.student.rcc.test.parallelism;

import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.jupiter.extension.UsersClientExtension;
import io.student.rcc.model.UserJson;
import io.student.rcc.service.UsersClient;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.ResourceLock;

@ExtendWith(UsersClientExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ParallelismTests {

  private UsersClient usersClient;

  @Test
  @Order(0)
  @ResourceLock(value = "userdata")
  void simpleParallelismFistTest() {
    List<UserJson> allUsers = usersClient.findAll();

    Assertions.assertTrue(allUsers.isEmpty());
  }

  @Test
  @Order(50)
  void simpleParallelismSecondTest() {
    UserJson user = new UserJson(UUID.randomUUID(), "local_user", null, null, null);

    Assertions.assertEquals("local_user", user.username());
  }

  @Test
  @Order(80)
  void simpleParallelismUsernameNotBlankTest() {
    UserJson user = new UserJson(UUID.randomUUID(), "local_user", null, null, null);

    Assertions.assertFalse(user.username().isBlank());
  }

  @User
  @Test
  @Order(100)
  @ResourceLock(value = "userdata")
  void simpleParallelismLastTest(UserJson user) {
    List<UserJson> allUsers = usersClient.findAll();

    Assertions.assertFalse(allUsers.isEmpty());
  }

}
