package io.student.rcc.service;

import static io.student.rcc.utils.RandomUserDataUtils.getRandomFirstName;
import static io.student.rcc.utils.RandomUserDataUtils.getRandomLastName;

import io.student.rcc.config.Config;
import io.student.rcc.model.UserJson;
import java.sql.PreparedStatement;
import java.util.UUID;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

public class UsersDbClient implements UsersClient {

  private static Config CFG = Config.getInstance();
  private final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

  @Override
  public UserJson createUser(String username, String password) {
    final JdbcTemplate jdbcTemplate = new JdbcTemplate(
        new SingleConnectionDataSource(
            CFG.authJdbcUrl(),
            CFG.dbUserName(),
            CFG.dbPassword(),
            false
        )
    );

    jdbcTemplate.update(
        con -> {
          PreparedStatement ps = con.prepareStatement(
              "INSERT INTO `user` "
                  + "(username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired)"
                  + " VALUES (?, ?, ?, ?, ?, ?)"
          );
          ps.setString(1, username);
          ps.setString(2, passwordEncoder.encode(password));
          ps.setBoolean(3, true);
          ps.setBoolean(4, true);
          ps.setBoolean(5, true);
          ps.setBoolean(6, true);
          return ps;
        }
    );

    return new UserJson(
        UUID.randomUUID(),
        username,
        getRandomFirstName(),
        getRandomLastName(),
        null
    );
  }
}
