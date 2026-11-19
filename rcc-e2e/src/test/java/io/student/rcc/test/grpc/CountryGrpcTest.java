package io.student.rcc.test.grpc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.student.rcc.jupiter.annotation.ApiLogin;
import io.student.rcc.jupiter.annotation.Country;
import io.student.rcc.jupiter.annotation.Token;
import io.student.rcc.jupiter.annotation.User;
import io.student.rcc.model.CountryJson;
import io.student.rococo.grpc.CountriesRequest;
import io.student.rococo.grpc.CountriesResponse;
import io.student.rococo.grpc.CountryByIdRequest;
import io.student.rococo.grpc.CountryByNameRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.jupiter.api.Test;

public class CountryGrpcTest extends BaseGrpcTest {

  @User
  @ApiLogin
  @Test
  void getCountries(@Token String bearerToken) {
    CountriesResponse response = withAuth(countryStub, bearerToken)
        .getCountries(CountriesRequest.newBuilder().setPage(0).setSize(10).build());

    assertEquals(10, response.getCountriesCount());
  }

  @User(countries = @Country(name = "grpc_country_by_id"))
  @ApiLogin
  @Test
  void getCountryById(@Token String bearerToken, CountryJson country) {
    io.student.rococo.grpc.Country response = withAuth(countryStub, bearerToken)
        .getCountryById(CountryByIdRequest.newBuilder().setId(country.id().toString()).build());

    assertEquals(country.id().toString(), response.getId());
    assertEquals(country.name(), response.getName());
  }

  @User(countries = @Country(name = "grpc_country_by_name"))
  @ApiLogin
  @Test
  void getCountryByName(@Token String bearerToken, CountryJson country) {
    io.student.rococo.grpc.Country response = withAuth(countryStub, bearerToken)
        .getCountryByName(CountryByNameRequest.newBuilder().setName(country.name()).build());

    assertEquals(country.id().toString(), response.getId());
    assertEquals(country.name(), response.getName());
  }

  @User
  @ApiLogin
  @Test
  void streamCountries(@Token String bearerToken) {
    Iterator<io.student.rococo.grpc.Country> stream = withAuth(countryStub, bearerToken)
        .streamCountries(CountriesRequest.newBuilder().setPage(0).setSize(10).build());

    List<io.student.rococo.grpc.Country> countries = new ArrayList<>();
    stream.forEachRemaining(countries::add);

    assertEquals(10, countries.size());
    assertFalse(countries.getFirst().getId().isBlank());
    assertFalse(countries.getFirst().getName().isBlank());
  }
}
