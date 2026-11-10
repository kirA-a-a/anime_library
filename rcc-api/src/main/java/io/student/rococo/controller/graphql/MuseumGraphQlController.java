package io.student.rococo.controller.graphql;

import io.student.rococo.controller.graphql.input.MuseumInput;
import io.student.rococo.model.MuseumJson;
import io.student.rococo.service.api.MuseumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

/**
 * GraphQL Query/Mutation для музеев — аналог {@code MuseumController}.
 */
@Controller
public class MuseumGraphQlController {

  private final MuseumService museumService;

  @Autowired
  public MuseumGraphQlController(MuseumService museumService) {
    this.museumService = museumService;
  }

  @QueryMapping
  public Slice<MuseumJson> museums(@Argument String title,
                                   @Argument int page,
                                   @Argument int size) {
    return museumService.all(title, PageRequest.of(page, size));
  }

  @QueryMapping
  public MuseumJson museum(@Argument String id) {
    return museumService.findById(id);
  }

  @MutationMapping
  public MuseumJson addMuseum(@Argument MuseumInput museum) {
    return museumService.create(museum.toJson());
  }

  @MutationMapping
  public MuseumJson updateMuseum(@Argument MuseumInput museum) {
    return museumService.update(museum.toJson());
  }
}
