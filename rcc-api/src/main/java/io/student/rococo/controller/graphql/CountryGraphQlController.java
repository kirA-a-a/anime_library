package io.student.rococo.controller.graphql;

import io.student.rococo.model.CountryJson;
import io.student.rococo.service.api.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * GraphQL Query для стран — аналог {@code CountryController} (только GET).
 */
@Controller
public class CountryGraphQlController {

  private final CountryService countryService;

  @Autowired
  public CountryGraphQlController(CountryService countryService) {
    this.countryService = countryService;
  }

  @QueryMapping
  public Slice<CountryJson> countries(@Argument String title,
                                      @Argument int page,
                                      @Argument int size) {
    return countryService.all(title, PageRequest.of(page, size));
  }

  @QueryMapping
  public CountryJson country(@Argument String id) {
    return countryService.findCountryById(id);
  }

  @QueryMapping
  public CountryJson countryByName(@Argument String name) {
    return countryService.findCountryByName(name);
  }
}
