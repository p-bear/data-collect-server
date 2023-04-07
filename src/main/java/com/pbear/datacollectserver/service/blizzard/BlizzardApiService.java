package com.pbear.datacollectserver.service.blizzard;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pbear.datacollectserver.data.blizzard.call.response.GetAuctionsRes;
import com.pbear.datacollectserver.data.blizzard.call.response.GetRealmRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlizzardApiService {
  private static final String REGION = "kr";
  private static final String NAMESPACE = "dynamic-kr";
  private static final String LOCALE = "ko_KR";

  private final Gson gson = new GsonBuilder()
      .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
      .create();

  @Value("${blizzard.api.uri}")
  private String BLIZZARD_API_URI;

  private final HttpClient httpClient;
  private final BlizzardAuthService blizzardAuthService;


  public GetAuctionsRes getAuctions(final long connectedRealmId) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(this.buildApiUri("/data/wow/connected-realm/" + connectedRealmId + "/auctions", this.buildDefaultParams()))
        .header("Authorization", this.buildAuthorizationHeader())
        .GET()
        .build();
    return this.sendRequest(request, GetAuctionsRes.class);
  }

  public GetRealmRes getRealm(final String realmSlug) throws IOException, InterruptedException {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(this.buildApiUri("/data/wow/realm/" + realmSlug, this.buildDefaultParams()))
        .header("Authorization", this.buildAuthorizationHeader())
        .GET()
        .build();
    return this.sendRequest(request, GetRealmRes.class);
  }

  private <T> T sendRequest(final HttpRequest httpRequest, final Class<T> responseClass) throws IOException, InterruptedException {
    HttpResponse<InputStream> response = this.httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());
    return this.gson.fromJson(new InputStreamReader(response.body()), responseClass);
  }

  private Map<String, String> buildDefaultParams() {
    Map<String, String> params = new HashMap<>();
    params.put("region", REGION);
    params.put("namespace", NAMESPACE);
    params.put("locale", LOCALE);
    return params;
  }

  private URI buildApiUri(final String path, final Map<String, String> parameters) {
    String uriStr = BLIZZARD_API_URI + path;
    if (parameters != null && parameters.size() > 0) {
      uriStr += "?";
      uriStr += parameters.entrySet().stream()
          .map(entry -> entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
          .collect(Collectors.joining("&"));
    }
    try {
      return new URI(uriStr);
    } catch (URISyntaxException e) {
      log.error("fail to get API uri, path: {}", path);
      throw new RuntimeException(e);
    }
  }

  private String buildAuthorizationHeader() {
    return "bearer " + this.blizzardAuthService.getAccessToken();
  }
}
