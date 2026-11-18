package io.student.rococo.controller.grpc;

import io.grpc.stub.StreamObserver;
import io.student.rococo.grpc.Country;
import io.student.rococo.grpc.Geo;
import io.student.rococo.grpc.Museum;
import io.student.rococo.grpc.MuseumByIdRequest;
import io.student.rococo.grpc.MuseumServiceGrpc;
import io.student.rococo.grpc.MuseumsRequest;
import io.student.rococo.grpc.MuseumsResponse;
import io.student.rococo.model.CountryJson;
import io.student.rococo.model.GeoJson;
import io.student.rococo.model.MuseumJson;
import io.student.rococo.service.api.MuseumService;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class MuseumGrpcService extends MuseumServiceGrpc.MuseumServiceImplBase {

  private final MuseumService museumService;

  public MuseumGrpcService(MuseumService museumService) {
    this.museumService = museumService;
  }

  @Override
  public void getMuseums(MuseumsRequest request, StreamObserver<MuseumsResponse> responseObserver) {
    Page<MuseumJson> page = museumService.all(
        request.getTitle().isBlank() ? null : request.getTitle(),
        PageRequest.of(request.getPage(), request.getSize())
    );

    MuseumsResponse.Builder response = MuseumsResponse.newBuilder()
        .setTotalCount((int) page.getTotalElements());
    page.forEach(museum -> response.addMuseums(toProto(museum)));

    responseObserver.onNext(response.build());
    responseObserver.onCompleted();
  }

  @Override
  public void getMuseumById(MuseumByIdRequest request, StreamObserver<Museum> responseObserver) {
    final MuseumJson museum = museumService.findById(request.getId());
    responseObserver.onNext(toProto(museum));
    responseObserver.onCompleted();
  }

  @Override
  public void createMuseum(Museum request, StreamObserver<Museum> responseObserver) {
    final MuseumJson saved = museumService.create(toJson(request));
    responseObserver.onNext(toProto(saved));
    responseObserver.onCompleted();
  }

  @Override
  public void updateMuseum(Museum request, StreamObserver<Museum> responseObserver) {
    final MuseumJson updated = museumService.update(toJson(request));
    responseObserver.onNext(toProto(updated));
    responseObserver.onCompleted();
  }

  private static Museum toProto(MuseumJson museum) {
    return Museum.newBuilder()
        .setId(museum.id().toString())
        .setTitle(museum.title())
        .setDescription(museum.description())
        .setPhoto(museum.photo() == null ? "" : museum.photo())
        .setGeo(Geo.newBuilder()
            .setCity(museum.geo().city())
            .setCountry(Country.newBuilder()
                .setId(museum.geo().country().id().toString())
                .setName(museum.geo().country().name())
                .build())
            .build())
        .build();
  }

  private static MuseumJson toJson(Museum request) {
    return new MuseumJson(
        request.getId().isBlank() ? null : UUID.fromString(request.getId()),
        request.getTitle(),
        request.getDescription(),
        request.getPhoto(),
        new GeoJson(
            request.getGeo().getCity(),
            new CountryJson(
                request.getGeo().getCountry().getId().isBlank()
                    ? null
                    : UUID.fromString(request.getGeo().getCountry().getId()),
                request.getGeo().getCountry().getName()
            )
        )
    );
  }
}
