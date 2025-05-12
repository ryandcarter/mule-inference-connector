package com.mulesoft.connectors.internal.connection.xinference;

import com.mulesoft.connectors.internal.connection.TextGenerationConnection;
import org.mule.runtime.http.api.client.HttpClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class XInferenceTextGenerationConnection extends TextGenerationConnection {

  private static final String URI_CHAT_COMPLETIONS = "/chat/completions";
  public static final String XINFERENCE_URL = "http://localhost:9997/v1";

  private final URL connectionURL;

  public XInferenceTextGenerationConnection(HttpClient httpClient, String modelName, String apiKey,
                                         Number temperature, Number topP,
                                         Number maxTokens, Map<String, String> mcpSseServers, int timeout)
          throws MalformedURLException {
    super(httpClient, apiKey, modelName, maxTokens, temperature, topP, timeout, mcpSseServers, fetchApiURL(), "XINFERENCE");
    this.connectionURL = new URL(XINFERENCE_URL + URI_CHAT_COMPLETIONS);
  }

  @Override
  public URL getConnectionURL() {
    return connectionURL;
  }

  @Override
  public Map<String, String> getQueryParams() {
    return Map.of();
  }

  @Override
  public Map<String, String> getAdditionalHeaders() {
    return Map.of("Authorization", "Bearer " + this.getApiKey());
  }

  private static String fetchApiURL() {
    return XINFERENCE_URL + URI_CHAT_COMPLETIONS;
  }
} 