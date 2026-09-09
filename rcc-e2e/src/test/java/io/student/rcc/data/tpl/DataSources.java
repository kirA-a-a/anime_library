package io.student.rcc.data.tpl;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import io.student.rcc.config.Config;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;

public class DataSources {

  private static final Config CFG = Config.getInstance();
  private static final Map<String, DataSource> dataSources = new ConcurrentHashMap<>();

  private DataSources() {
  }

  public static DataSource dataSource(String jdbcUrl) {
    return dataSources.computeIfAbsent(jdbcUrl, key -> {
      AtomikosDataSourceBean dsBean = new AtomikosDataSourceBean();
      final String uniqId = StringUtils.substringAfter(key, "3306/").split("\\?")[0];
      dsBean.setUniqueResourceName(uniqId);
      dsBean.setXaDataSourceClassName("com.mysql.cj.jdbc.MysqlXADataSource");

      Properties props = new Properties();
      props.put("url", key);
      props.put("user", CFG.dbUserName());
      props.put("password", CFG.dbPassword());

      dsBean.setXaProperties(props);
      dsBean.setPoolSize(3);
      dsBean.setMaxPoolSize(10);

      try {
        InitialContext context = new InitialContext();
        context.bind("java:comp/env/jdbc/" + uniqId, dsBean);
      } catch (NamingException e) {
        throw new RuntimeException(e);
      }

      return dsBean;
    });
  }
}
