package com.pbear.datacollectserver.service.blizzard;

import com.google.gson.Gson;
import com.pbear.datacollectserver.data.blizzard.call.response.AccessTokenRes;
import com.pbear.datacollectserver.data.exception.BlizzardTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class BlizzardAuthService {
  private final HttpClient httpClient;

  private final Map<String, AccessTokenRes> accessTokenCacheMap = new ConcurrentHashMap<>();
  private final Gson gson = new Gson();

  @Value("${blizzard.auth.uri}")
  private String BLIZZARD_AUTH_URI;
  @Value("${blizzard.auth.clientId}")
  private String BLIZZARD_AUTH_CLIENT_ID;
  @Value("${blizzard.auth.clientSecret}")
  private String BLIZZARD_AUTH_CLIENT_SECRET;

  @PostConstruct
  public void init() {
    this.getAccessToken();
  }

  public String getAccessToken() {
    if (this.accessTokenCacheMap.isEmpty() || this.isTokenExpired(this.accessTokenCacheMap.get("accessToken").getExpiredDate())) {
      try {
        this.accessTokenCacheMap.put("accessToken", this.convertToAccessToken(this.postClientCredentialAccessToken()));
      } catch (Exception e) {
        log.error("fail to get accessToken from blizzard", e);
        throw new BlizzardTokenException();
      }
    }

    return this.accessTokenCacheMap.get("accessToken").getAccessToken();
  }

  private AccessTokenRes convertToAccessToken(final Map<String, Object> tokenResponse) {
    return new AccessTokenRes(
        String.valueOf(tokenResponse.get("access_token")),
        String.valueOf(tokenResponse.get("token_type")),
        LocalDateTime.now(ZoneId.systemDefault()),
        this.calcExpiredDate(((Double) tokenResponse.get("expires_in")).longValue()),
        String.valueOf(tokenResponse.get("sub")));
  }

  private boolean isTokenExpired(final LocalDateTime expiredDateTime) {
    return LocalDateTime.now(ZoneId.systemDefault()).isAfter(expiredDateTime);
  }

  @SuppressWarnings("unchecked")
  private Map<String, Object> postClientCredentialAccessToken() throws URISyntaxException, IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(new URI(BLIZZARD_AUTH_URI + "/token"))
        .header("Authorization", this.getBasicAuthHeaderValue(BLIZZARD_AUTH_CLIENT_ID, BLIZZARD_AUTH_CLIENT_SECRET))
        .header("Content-Type", "application/x-www-form-urlencoded")
        .POST(HttpRequest.BodyPublishers.ofString(
            Map.of("grant_type", "client_credentials").entrySet().stream()
                .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"))
        ))
        .build();
    HttpResponse<String> response = this.httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    return (Map<String, Object>) this.gson.fromJson(response.body(), Map.class);
  }

  private String getBasicAuthHeaderValue(final String clientId, final String clientSecret) {
    return "Basic " + Base64.getEncoder().encodeToString((clientId + ":" + clientSecret).getBytes());
  }

  public LocalDateTime calcExpiredDate(final long expiredIn) {
    return LocalDateTime.now(ZoneId.systemDefault())
        .plusSeconds(expiredIn);
  }
}
