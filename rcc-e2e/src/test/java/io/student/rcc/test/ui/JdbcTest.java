package io.student.rcc.test.ui;

import static io.student.rcc.utils.RandomUserDataUtils.getRandomUserName;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.student.rcc.config.Config;
import io.student.rcc.service.UsersDbClient;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class JdbcTest {

  private static final Config CFG = Config.getInstance();
  private static final String PASSWORD = "12345";

  private final UsersDbClient usersDbClient = new UsersDbClient();
  private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
  private final JdbcTemplate jdbcTemplate = new JdbcTemplate(
      new SingleConnectionDataSource(
          CFG.authJdbcUrl(),
          CFG.dbUserName(),
          CFG.dbPassword(),
          true
      )
  );

  @Test
  void shouldCreateUserInAuthDatabase() {
    String username = getRandomUserName();

    UUID id = usersDbClient.createUser(username, PASSWORD);

    AuthUserRow user = findUser(id);
    assertNotNull(id);
    assertEquals(username, user.username());
    assertTrue(user.enabled());
    assertTrue(user.accountNonExpired());
    assertTrue(user.accountNonLocked());
    assertTrue(user.credentialsNonExpired());
    assertTrue(passwordEncoder.matches(PASSWORD, user.password()));
    assertEquals(0, countAuthorities(id));
  }

  @ParameterizedTest
  @ValueSource(strings = {"read", "write"})
  void shouldCreateUserWithAuthorityInAuthDatabase(String authority) {
    String username = getRandomUserName();

    UUID id = usersDbClient.createUser(authority, username, PASSWORD);

    AuthUserRow user = findUser(id);
    assertNotNull(id);
    assertEquals(username, user.username());
    assertTrue(passwordEncoder.matches(PASSWORD, user.password()));
    assertEquals(authority, findAuthority(id));
  }

  private AuthUserRow findUser(UUID id) {
    return jdbcTemplate.queryForObject(
        "SELECT BIN_TO_UUID(id, true) AS id, username, password, enabled, "
            + "account_non_expired, account_non_locked, credentials_non_expired "
            + "FROM `user` WHERE id = UUID_TO_BIN(?, true)",
        (rs, rowNum) -> new AuthUserRow(
            UUID.fromString(rs.getString("id")),
            rs.getString("username"),
            rs.getString("password"),
            rs.getBoolean("enabled"),
            rs.getBoolean("account_non_expired"),
            rs.getBoolean("account_non_locked"),
            rs.getBoolean("credentials_non_expired")
        ),
        id.toString()
    );
  }

  private String findAuthority(UUID userId) {
    return jdbcTemplate.queryForObject(
        "SELECT authority FROM `authority` WHERE user_id = UUID_TO_BIN(?, true)",
        String.class,
        userId.toString()
    );
  }

  private Integer countAuthorities(UUID userId) {
    return jdbcTemplate.queryForObject(
        "SELECT COUNT(*) FROM `authority` WHERE user_id = UUID_TO_BIN(?, true)",
        Integer.class,
        userId.toString()
    );
  }

  private record AuthUserRow(
      UUID id,
      String username,
      String password,
      boolean enabled,
      boolean accountNonExpired,
      boolean accountNonLocked,
      boolean credentialsNonExpired
  ) {
  }
}
