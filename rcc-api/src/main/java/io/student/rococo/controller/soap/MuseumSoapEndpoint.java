package io.student.rococo.controller.soap;

import static io.student.rococo.config.RococoApiConfiguration.SOAP_NAMESPACE;

import io.student.rococo.model.CountryJson;
import io.student.rococo.model.GeoJson;
import io.student.rococo.model.MuseumJson;
import io.student.rococo.service.api.MuseumService;
import io.student.rococo.soap.Country;
import io.student.rococo.soap.CreateMuseumRequest;
import io.student.rococo.soap.Geo;
import io.student.rococo.soap.Museum;
import io.student.rococo.soap.MuseumByIdRequest;
import io.student.rococo.soap.MuseumResponse;
import io.student.rococo.soap.MuseumsRequest;
import io.student.rococo.soap.MuseumsResponse;
import io.student.rococo.soap.UpdateMuseumRequest;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class MuseumSoapEndpoint {

  private final MuseumService museumService;

  @Autowired
  public MuseumSoapEndpoint(MuseumService museumService) {
    this.museumService = museumService;
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "museumsRequest")
  @ResponsePayload
  public MuseumsResponse museums(@RequestPayload MuseumsRequest request) {
    String title = (request.getTitle() == null || request.getTitle().isBlank())
        ? null
        : request.getTitle();
    Page<MuseumJson> page = museumService.all(
        title,
        PageRequest.of(request.getPage(), request.getSize())
    );

    MuseumsResponse response = new MuseumsResponse();
    response.setTotalCount((int) page.getTotalElements());
    page.forEach(museum -> response.getMuseums().add(toSoap(museum)));
    return response;
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "museumByIdRequest")
  @ResponsePayload
  public MuseumResponse museumById(@RequestPayload MuseumByIdRequest request) {
    MuseumJson museum = museumService.findById(request.getId());
    MuseumResponse response = new MuseumResponse();
    response.setMuseum(toSoap(museum));
    return response;
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "createMuseumRequest")
  @ResponsePayload
  public MuseumResponse createMuseum(@RequestPayload CreateMuseumRequest request) {
    MuseumJson saved = museumService.create(toJson(request.getMuseum()));
    MuseumResponse response = new MuseumResponse();
    response.setMuseum(toSoap(saved));
    return response;
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "updateMuseumRequest")
  @ResponsePayload
  public MuseumResponse updateMuseum(@RequestPayload UpdateMuseumRequest request) {
    MuseumJson updated = museumService.update(toJson(request.getMuseum()));
    MuseumResponse response = new MuseumResponse();
    response.setMuseum(toSoap(updated));
    return response;
  }

  private static Museum toSoap(MuseumJson museum) {
    Country country = new Country();
    country.setId(museum.geo().country().id().toString());
    country.setName(museum.geo().country().name());

    Geo geo = new Geo();
    geo.setCity(museum.geo().city());
    geo.setCountry(country);

    Museum soap = new Museum();
    soap.setId(museum.id().toString());
    soap.setTitle(museum.title());
    soap.setDescription(museum.description());
    soap.setPhoto(museum.photo() == null ? "" : museum.photo());
    soap.setGeo(geo);
    return soap;
  }

  private static MuseumJson toJson(Museum museum) {
    UUID id = (museum.getId() == null || museum.getId().isBlank())
        ? null
        : UUID.fromString(museum.getId());
    String countryId = museum.getGeo().getCountry().getId();
    return new MuseumJson(
        id,
        museum.getTitle(),
        museum.getDescription(),
        museum.getPhoto(),
        new GeoJson(
            museum.getGeo().getCity(),
            new CountryJson(
                (countryId == null || countryId.isBlank()) ? null : UUID.fromString(countryId),
                museum.getGeo().getCountry().getName()
            )
        )
    );
  }
}
