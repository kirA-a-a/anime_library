package io.student.rococo.config;

import static org.springframework.http.HttpMethod.GET;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.student.rococo.service.cors.CorsCustomizer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.SimpleXsdSchema;
import org.springframework.xml.xsd.XsdSchema;

@EnableWs
@EnableWebSecurity
@Configuration
public class RococoApiConfiguration {

  public static final String SOAP_NAMESPACE = "rococo";

  private final CorsCustomizer corsCustomizer;
  private final String rococoApiBaseUri;

  @Autowired
  public RococoApiConfiguration(
      CorsCustomizer corsCustomizer,
      @Value("${rococo-api.base-uri}") String rococoApiBaseUri
  ) {
    this.corsCustomizer = corsCustomizer;
    this.rococoApiBaseUri = rococoApiBaseUri;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    corsCustomizer.apply(http);
    http.csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(customizer ->
            customizer
                .requestMatchers(GET, "/api/session").permitAll()
                .requestMatchers(GET, "/api/artist/**").permitAll()
                .requestMatchers(GET, "/api/museum/**").permitAll()
                .requestMatchers(GET, "/api/painting/**").permitAll()
                .requestMatchers(GET, "/api/country/**").permitAll()
                .requestMatchers("/graphql", "/graphiql", "/graphiql/**").permitAll()
                .requestMatchers("/ws/**").permitAll()
                .anyRequest().authenticated()
        )
        .oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));
    return http.build();
  }

  @Bean
  public ObjectMapper objectMapper() {

    ObjectMapper objectMapper = new ObjectMapper();
    DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
    objectMapper.setDateFormat(dtf);

    return objectMapper;
  }

  @Bean
  public ServletRegistrationBean<MessageDispatcherServlet> messageDispatcherServlet(
      ApplicationContext applicationContext
  ) {
    MessageDispatcherServlet servlet = new MessageDispatcherServlet();
    servlet.setApplicationContext(applicationContext);
    servlet.setTransformWsdlLocations(true);
    return new ServletRegistrationBean<>(servlet, "/ws/*");
  }

  @Bean(name = "rcc")
  public DefaultWsdl11Definition defaultWsdl11Definition(XsdSchema rccSchema) {
    DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
    wsdl11Definition.setPortTypeName("RococoPort");
    wsdl11Definition.setLocationUri(rococoApiBaseUri + "/ws");
    wsdl11Definition.setTargetNamespace(SOAP_NAMESPACE);
    wsdl11Definition.setSchema(rccSchema);
    return wsdl11Definition;
  }

  @Bean
  public XsdSchema rccSchema() {
    return new SimpleXsdSchema(new ClassPathResource("rcc.xsd"));
  }

}
