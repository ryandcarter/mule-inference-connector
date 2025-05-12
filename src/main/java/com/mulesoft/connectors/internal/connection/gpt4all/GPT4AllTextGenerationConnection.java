package com.mulesoft.connectors.internal.connection.gpt4all;

import com.mulesoft.connectors.internal.connection.TextGenerationConnection;
import com.mulesoft.connectors.internal.constants.InferenceConstants;
import org.mule.runtime.http.api.client.HttpClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class GPT4AllTextGenerationConnection extends TextGenerationConnection {

  private static final String URI_CHAT_COMPLETIONS = "/chat/completions";
  public static final String GPT4ALL_URL = "http://localhost:4891/v1";

  private final URL connectionURL;

  public GPT4AllTextGenerationConnection(HttpClient httpClient, String modelName, String apiKey,
                                         Number temperature, Number topP,
                                         Number maxTokens, Map<String, String> mcpSseServers, int timeout)
          throws MalformedURLException {
    super(httpClient, apiKey, modelName, maxTokens, temperature, topP, timeout, mcpSseServers, fetchApiURL(), "GPT4ALL");
    this.connectionURL = new URL(GPT4ALL_URL + URI_CHAT_COMPLETIONS);
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
    return GPT4ALL_URL + URI_CHAT_COMPLETIONS;
  }
} 