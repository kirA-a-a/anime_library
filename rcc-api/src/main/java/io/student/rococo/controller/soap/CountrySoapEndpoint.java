package io.student.rococo.controller.soap;

import static io.student.rococo.config.RococoApiConfiguration.SOAP_NAMESPACE;

import io.student.rococo.model.CountryJson;
import io.student.rococo.service.api.CountryService;
import io.student.rococo.soap.CountriesRequest;
import io.student.rococo.soap.CountriesResponse;
import io.student.rococo.soap.Country;
import io.student.rococo.soap.CountryByIdRequest;
import io.student.rococo.soap.CountryByNameRequest;
import io.student.rococo.soap.CountryResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class CountrySoapEndpoint {

  private final CountryService countryService;

  @Autowired
  public CountrySoapEndpoint(CountryService countryService) {
    this.countryService = countryService;
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "countriesRequest")
  @ResponsePayload
  public CountriesResponse countries(@RequestPayload CountriesRequest request) {
    String title = (request.getTitle() == null || request.getTitle().isBlank())
        ? null
        : request.getTitle();
    Page<CountryJson> page = countryService.all(
        title,
        PageRequest.of(request.getPage(), request.getSize())
    );

    CountriesResponse response = new CountriesResponse();
    response.setTotalCount((int) page.getTotalElements());
    page.forEach(country -> response.getCountries().add(toSoap(country)));
    return response;
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "countryByIdRequest")
  @ResponsePayload
  public CountryResponse countryById(@RequestPayload CountryByIdRequest request) {
    CountryJson country = countryService.findCountryById(request.getId());
    CountryResponse response = new CountryResponse();
    response.setCountry(toSoap(country));
    return response;
  }

  @PayloadRoot(namespace = SOAP_NAMESPACE, localPart = "countryByNameRequest")
  @ResponsePayload
  public CountryResponse countryByName(@RequestPayload CountryByNameRequest request) {
    CountryJson country = countryService.findCountryByName(request.getName());
    CountryResponse response = new CountryResponse();
    response.setCountry(toSoap(country));
    return response;
  }

  private static Country toSoap(CountryJson country) {
    Country soap = new Country();
    soap.setId(country.id().toString());
    soap.setName(country.name());
    return soap;
  }
}
