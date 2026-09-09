package io.student.rcc.data.jpa;

import io.student.rcc.config.Config;
import io.student.rcc.data.tpl.DataSources;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityManagers {

  private static final Config CFG = Config.getInstance();
  private static final Map<String, EntityManagerFactory> emfs = new ConcurrentHashMap<>();

  private EntityManagers() {
  }

  public static EntityManager em(String jdbcUrl) {
    return new ThreadSafeEntityManager(emfs.computeIfAbsent(jdbcUrl, key -> {
          DataSources.dataSource(jdbcUrl);
          return Persistence.createEntityManagerFactory(jdbcUrl);
        }
    ).createEntityManager());
  }
}
