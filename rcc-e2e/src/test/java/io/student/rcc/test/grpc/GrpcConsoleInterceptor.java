package io.student.rcc.test.grpc;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.MessageOrBuilder;
import com.google.protobuf.util.JsonFormat;
import io.grpc.CallOptions;
import io.grpc.Channel;
import io.grpc.ClientCall;
import io.grpc.ClientInterceptor;
import io.grpc.ForwardingClientCall;
import io.grpc.ForwardingClientCallListener;
import io.grpc.Metadata;
import io.grpc.MethodDescriptor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GrpcConsoleInterceptor implements ClientInterceptor {

  private static final JsonFormat.Printer printer = JsonFormat.printer();

  @Override
  public <ReqT, RespT> ClientCall<ReqT, RespT> interceptCall(
      MethodDescriptor<ReqT, RespT> methodDescriptor,
      CallOptions callOptions,
      Channel next
  ) {
    return new ForwardingClientCall.SimpleForwardingClientCall<>(
        next.newCall(methodDescriptor, callOptions)
    ) {
      @Override
      public void sendMessage(ReqT message) {
        try {
          log.info("REQUEST: {}", printer.print((MessageOrBuilder) message));
        } catch (InvalidProtocolBufferException e) {
          throw new RuntimeException(e);
        }
        super.sendMessage(message);
      }

      @Override
      public void start(Listener<RespT> responseListener, Metadata headers) {
        ForwardingClientCallListener<Object> clientCallListener =
            new ForwardingClientCallListener<>() {
              @Override
              public void onMessage(Object message) {
                try {
                  log.info("RESPONSE: {}", printer.print((MessageOrBuilder) message));
                } catch (InvalidProtocolBufferException e) {
                  throw new RuntimeException(e);
                }
                super.onMessage(message);
              }

              @Override
              protected Listener<Object> delegate() {
                return (Listener<Object>) responseListener;
              }
            };
        super.start((Listener<RespT>) clientCallListener, headers);
      }
    };
  }
}
