package com.mulesoft.connectors.internal.connection.ai21labs;

import com.mulesoft.connectors.internal.connection.TextGenerationConnection;
import org.mule.runtime.http.api.client.HttpClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class AI21LabsTextGenerationConnection extends TextGenerationConnection {

  private static final String URI_CHAT_COMPLETIONS = "/chat/completions";
  public static final String AI21LABS_URL = "https://api.ai21.com/studio/v1";

  private final URL connectionURL;

  public AI21LabsTextGenerationConnection(HttpClient httpClient, String modelName, String apiKey,
                                         Number temperature, Number topP,
                                         Number maxTokens, Map<String, String> mcpSseServers, int timeout)
          throws MalformedURLException {
    super(httpClient, apiKey, modelName, maxTokens, temperature, topP, timeout, mcpSseServers, fetchApiURL(), "AI21LABS");
    this.connectionURL = new URL(AI21LABS_URL + URI_CHAT_COMPLETIONS);
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
    return AI21LABS_URL + URI_CHAT_COMPLETIONS;
  }
} 