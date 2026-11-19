package io.student.rococo.controller.grpc;

import io.grpc.stub.StreamObserver;
import io.student.rococo.grpc.CountriesRequest;
import io.student.rococo.grpc.CountriesResponse;
import io.student.rococo.grpc.Country;
import io.student.rococo.grpc.CountryByIdRequest;
import io.student.rococo.grpc.CountryByNameRequest;
import io.student.rococo.grpc.CountryServiceGrpc;
import io.student.rococo.model.CountryJson;
import io.student.rococo.service.api.CountryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.grpc.server.service.GrpcService;

@GrpcService
public class CountryGrpcService extends CountryServiceGrpc.CountryServiceImplBase {

  private final CountryService countryService;

  public CountryGrpcService(CountryService countryService) {
    this.countryService = countryService;
  }

  @Override
  public void getCountries(CountriesRequest request, StreamObserver<CountriesResponse> responseObserver) {
    Page<CountryJson> page = countryService.all(
        request.getTitle().isBlank() ? null : request.getTitle(),
        PageRequest.of(request.getPage(), request.getSize())
    );

    CountriesResponse.Builder response = CountriesResponse.newBuilder()
        .setTotalCount((int) page.getTotalElements());
    page.forEach(country -> response.addCountries(
        Country.newBuilder()
            .setId(country.id().toString())
            .setName(country.name())
            .build()
    ));

    responseObserver.onNext(response.build());
    responseObserver.onCompleted();
  }

  @Override
  public void getCountryById(CountryByIdRequest request, StreamObserver<Country> responseObserver) {
    final CountryJson country = countryService.findCountryById(request.getId());
    responseObserver.onNext(
        Country.newBuilder()
            .setId(country.id().toString())
            .setName(country.name())
            .build()
    );
    responseObserver.onCompleted();
  }

  @Override
  public void getCountryByName(CountryByNameRequest request, StreamObserver<Country> responseObserver) {
    final CountryJson country = countryService.findCountryByName(request.getName());
    responseObserver.onNext(
        Country.newBuilder()
            .setId(country.id().toString())
            .setName(country.name())
            .build()
    );
    responseObserver.onCompleted();
  }

  @Override
  public void streamCountries(CountriesRequest request, StreamObserver<Country> responseObserver) {
    Page<CountryJson> page = countryService.all(
        request.getTitle().isBlank() ? null : request.getTitle(),
        PageRequest.of(request.getPage(), request.getSize())
    );

    page.forEach(country -> responseObserver.onNext(
        Country.newBuilder()
            .setId(country.id().toString())
            .setName(country.name())
            .build()
    ));
    responseObserver.onCompleted();
  }
}
