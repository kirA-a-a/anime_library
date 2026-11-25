package io.student.rcc.test.grpc;

import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.stub.AbstractStub;
import io.grpc.stub.MetadataUtils;
import io.student.rcc.config.Config;
import io.student.rcc.jupiter.annotation.meta.GrpcTest;
import io.student.rcc.jupiter.extension.ApiLoginExtension;
import io.student.rococo.grpc.ArtistServiceGrpc;
import io.student.rococo.grpc.ArtistServiceGrpc.ArtistServiceBlockingStub;
import io.student.rococo.grpc.CountryServiceGrpc;
import io.student.rococo.grpc.CountryServiceGrpc.CountryServiceBlockingStub;
import io.student.rococo.grpc.MuseumServiceGrpc;
import io.student.rococo.grpc.MuseumServiceGrpc.MuseumServiceBlockingStub;
import org.junit.jupiter.api.extension.RegisterExtension;

@GrpcTest
public class BaseGrpcTest {

  @RegisterExtension
  protected static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

  protected static final Config CFG = Config.getInstance();

  protected static final Channel channel = ManagedChannelBuilder
      .forAddress(CFG.apiGrpcAddress(), CFG.apiGrpcPort())
      .intercept(new GrpcConsoleInterceptor())
      .usePlaintext()
      .build();

  protected static final CountryServiceBlockingStub countryStub =
      CountryServiceGrpc.newBlockingStub(channel);

  protected static final ArtistServiceBlockingStub artistStub =
      ArtistServiceGrpc.newBlockingStub(channel);

  protected static final MuseumServiceBlockingStub museumStub =
      MuseumServiceGrpc.newBlockingStub(channel);

  protected static <T extends AbstractStub<T>> T withAuth(T stub, String bearerToken) {
    Metadata metadata = new Metadata();
    metadata.put(
        Metadata.Key.of("Authorization", Metadata.ASCII_STRING_MARSHALLER),
        bearerToken
    );
    return stub.withInterceptors(MetadataUtils.newAttachHeadersInterceptor(metadata));
  }
}
