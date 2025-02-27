package com.farukgenc.boilerplate.springboot.service;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;

@Service
public class CamundaIdentity {

  private final WebClient webClient;
  private final LoadingCache<String, TokenInfo> tokenCache;

  @Value("${camunda.oauth.token-url}")
  private String tokenUrl;

  @Value("${camunda.oauth.client-id}")
  private String clientId;

  @Value("${camunda.oauth.client-secret}")
  private String clientSecret;

  public CamundaIdentity(WebClient.Builder webClientBuilder) {
    this.webClient = webClientBuilder
        .baseUrl("http://localhost:18080/auth/realms/camunda-platform/protocol/openid-connect/token").build();
    this.tokenCache = Caffeine.newBuilder()
        .expireAfterWrite(Duration.ofMinutes(4))
        .build(key -> fetchNewToken());
  }

  public String getAccessToken() {
    return tokenCache.get("token").token;
  }

  private TokenInfo fetchNewToken() {
    MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
    formData.add("grant_type", "client_credentials");
    formData.add("client_id", clientId);
    formData.add("client_secret", clientSecret);

    Mono<Map> responseMono = webClient.post()
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .body(BodyInserters.fromFormData(formData))
        .retrieve()
        .bodyToMono(Map.class);

    // Block to get the result synchronously (for simplicity)
    Map<String, Object> response = responseMono.block();

    String token = (String) response.get("access_token");
    Integer expiresIn = (Integer) response.get("expires_in");

    Instant expiry = Instant.now().plusSeconds(expiresIn);

    return new TokenInfo(token, expiry);
  }

  private static class TokenInfo {
    String token;
    Instant expiry;

    TokenInfo(String token, Instant expiry) {
      this.token = token;
      this.expiry = expiry;
    }
  }
}
