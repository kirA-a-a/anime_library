package io.student.rococo.controller.soap;

import static io.student.rococo.config.RococoApiConfiguration.SOAP_NAMESPACE;

import io.student.rococo.model.ArtistJson;
import io.student.rococo.service.ArtistService;
import io.student.rococo.soap.Artist;
import io.student.rococo.soap.ArtistByIdRequest;
import io.student.rococo.soap.ArtistResponse;
import io.student.rococo.soap.ArtistsRequest;
import io.student.rococo.soap.ArtistsResponse;
import io.student.rococo.soap.CreateArtistRequest;
import io.student.rococo.soap.UpdateArtistRequest;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class ArtistSoapEndpoint {

  private final ArtistService artistService;

  @Autowired
  public ArtistSoapEndpoint(ArtistService artistService) {
    this.artistService = artistService;
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "artistsRequest")
  @ResponsePayload
  public ArtistsResponse artists(@RequestPayload ArtistsRequest request) {
    PageRequest pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<ArtistJson> page = (request.getName() == null || request.getName().isBlank())
        ? artistService.getArtists(pageable)
        : artistService.getArtistByName(pageable, request.getName());

    ArtistsResponse response = new ArtistsResponse();
    response.setTotalCount((int) page.getTotalElements());
    page.forEach(artist -> response.getArtists().add(toSoap(artist)));
    return response;
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "artistByIdRequest")
  @ResponsePayload
  public ArtistResponse artistById(@RequestPayload ArtistByIdRequest request) {
    ArtistJson artist = artistService.getArtistById(request.getId());
    ArtistResponse response = new ArtistResponse();
    response.setArtist(toSoap(artist));
    return response;
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "createArtistRequest")
  @ResponsePayload
  public ArtistResponse createArtist(@RequestPayload CreateArtistRequest request) {
    ArtistJson input = toJson(request.getArtist());
    // id на create всегда генерит сервер
    ArtistJson saved = artistService.saveArtist(new ArtistJson(
        null,
        input.name(),
        input.biography(),
        input.photo()
    ));
    ArtistResponse response = new ArtistResponse();
    response.setArtist(toSoap(saved));
    return response;
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "updateArtistRequest")
  @ResponsePayload
  public ArtistResponse updateArtist(@RequestPayload UpdateArtistRequest request) {
    ArtistJson updated = artistService.updateArtist(toJson(request.getArtist()));
    ArtistResponse response = new ArtistResponse();
    response.setArtist(toSoap(updated));
    return response;
  }

  private static Artist toSoap(ArtistJson artist) {
    Artist soap = new Artist();
    soap.setId(artist.id().toString());
    soap.setName(artist.name());
    soap.setBiography(artist.biography());
    soap.setPhoto(artist.photo() == null ? "" : artist.photo());
    return soap;
  }

  private static ArtistJson toJson(Artist artist) {
    UUID id = (artist.getId() == null || artist.getId().isBlank())
        ? null
        : UUID.fromString(artist.getId());
    return new ArtistJson(
        id,
        artist.getName(),
        artist.getBiography(),
        artist.getPhoto()
    );
  }
}
