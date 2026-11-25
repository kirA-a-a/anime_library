package io.student.rococo.controller.grpc;

import io.grpc.stub.StreamObserver;
import io.student.rococo.grpc.Artist;
import io.student.rococo.grpc.ArtistByIdRequest;
import io.student.rococo.grpc.ArtistServiceGrpc;
import io.student.rococo.grpc.ArtistsRequest;
import io.student.rococo.grpc.ArtistsResponse;
import io.student.rococo.model.ArtistJson;
import io.student.rococo.service.ArtistService;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class ArtistGrpcService extends ArtistServiceGrpc.ArtistServiceImplBase {

  private final ArtistService artistService;

  public ArtistGrpcService(ArtistService artistService) {
    this.artistService = artistService;
  }

  @Override
  public void getArtists(ArtistsRequest request, StreamObserver<ArtistsResponse> responseObserver) {
    PageRequest pageable = PageRequest.of(request.getPage(), request.getSize());
    Page<ArtistJson> page = request.getName().isBlank()
        ? artistService.getArtists(pageable)
        : artistService.getArtistByName(pageable, request.getName());

    ArtistsResponse.Builder response = ArtistsResponse.newBuilder()
        .setTotalCount((int) page.getTotalElements());
    page.forEach(artist -> response.addArtists(
        Artist.newBuilder()
            .setId(artist.id().toString())
            .setName(artist.name())
            .setBiography(artist.biography())
            .setPhoto(artist.photo() == null ? "" : artist.photo())
            .build()
    ));

    responseObserver.onNext(response.build());
    responseObserver.onCompleted();
  }

  @Override
  public void getArtistById(ArtistByIdRequest request, StreamObserver<Artist> responseObserver) {
    final ArtistJson artist = artistService.getArtistById(request.getId());
    responseObserver.onNext(
        Artist.newBuilder()
            .setId(artist.id().toString())
            .setName(artist.name())
            .setBiography(artist.biography())
            .setPhoto(artist.photo() == null ? "" : artist.photo())
            .build()
    );
    responseObserver.onCompleted();
  }

  @Override
  public void createArtist(Artist request, StreamObserver<Artist> responseObserver) {
    final ArtistJson saved = artistService.saveArtist(new ArtistJson(
        null,
        request.getName(),
        request.getBiography(),
        request.getPhoto()
    ));
    responseObserver.onNext(
        Artist.newBuilder()
            .setId(saved.id().toString())
            .setName(saved.name())
            .setBiography(saved.biography())
            .setPhoto(saved.photo() == null ? "" : saved.photo())
            .build()
    );
    responseObserver.onCompleted();
  }

  @Override
  public void updateArtist(Artist request, StreamObserver<Artist> responseObserver) {
    final ArtistJson updated = artistService.updateArtist(new ArtistJson(
        UUID.fromString(request.getId()),
        request.getName(),
        request.getBiography(),
        request.getPhoto()
    ));
    responseObserver.onNext(
        Artist.newBuilder()
            .setId(updated.id().toString())
            .setName(updated.name())
            .setBiography(updated.biography())
            .setPhoto(updated.photo() == null ? "" : updated.photo())
            .build()
    );
    responseObserver.onCompleted();
  }
}
