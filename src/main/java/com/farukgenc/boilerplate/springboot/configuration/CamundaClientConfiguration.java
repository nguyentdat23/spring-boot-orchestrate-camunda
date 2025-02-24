package com.farukgenc.boilerplate.springboot.configuration;

import io.camunda.client.CamundaClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class CamundaClientConfiguration {

  private static final Logger LOG = LoggerFactory.getLogger(CamundaClientConfiguration.class);

  @Value("${camunda.client.grpc-address:http://localhost:26500}") // Default value provided
  private String grpcAddress;

  @Value("${camunda.client.rest-address:http://localhost:8088}") // Default value provided
  private String restAddress;

  @Value("${camunda.client.use-plaintext:true}") // Default to true
  private boolean usePlaintext;

  @Bean
  public CamundaClient camundaClient() {
    try {
      CamundaClient client = CamundaClient.newClientBuilder()
          .grpcAddress(URI.create(grpcAddress))
          .restAddress(URI.create(restAddress))
          .usePlaintext()
          .build();

      LOG.info("Camunda Client initialized with gRPC address: {}, REST address: {}, plaintext: {}", grpcAddress,
          restAddress, usePlaintext);
      return client;

    } catch (Exception exception) {
      LOG.error("Error initializing Camunda Client: {}", exception.getMessage());
      throw new IllegalStateException("Failed to initialize Camunda Client", exception); // Better exception handling
    }
  }
}
