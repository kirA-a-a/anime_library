package io.student.rcc.test.graphql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import io.student.rcc.CountriesQuery;
import io.student.rcc.CountriesQuery.Data;
import io.student.rcc.jupiter.annotation.ApiLogin;
import io.student.rcc.jupiter.annotation.Token;
import io.student.rcc.jupiter.annotation.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CountryGraphQlTest extends BaseGraphQlTest{

  @User
  @Test
  @ApiLogin
  public void testCountryGraphQl(@Token String bearerToken) {
    ApolloCall<Data> authorization = apolloClient.query(CountriesQuery.builder().page(0).size(30).build())
        .addHttpHeader("Authorization", bearerToken);

    ApolloResponse<Data> response = Rx2Apollo.single(authorization).blockingGet();

    final Data data = response.dataOrThrow();

    data.countries.edges.forEach(el -> System.out.println("###### DATA " + el.node));

    Assertions.assertEquals(30, data.countries.edges.size());
  }
}
