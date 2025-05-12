package com.mulesoft.connectors.internal.connection.deepseek;

import com.mulesoft.connectors.internal.connection.TextGenerationConnection;
import com.mulesoft.connectors.internal.constants.InferenceConstants;
import org.mule.runtime.http.api.client.HttpClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class DeepseekTextGenerationConnection extends TextGenerationConnection {

  private static final String URI_CHAT_COMPLETIONS = "/chat/completions";
  public static final String DEEPSEEK_URL = "https://api.deepseek.com";

  private final URL connectionURL;

  public DeepseekTextGenerationConnection(HttpClient httpClient, String modelName, String apiKey,
                                         Number temperature, Number topP,
                                         Number maxTokens, Map<String, String> mcpSseServers, int timeout)
          throws MalformedURLException {
    super(httpClient, apiKey, modelName, maxTokens, temperature, topP, timeout, mcpSseServers, fetchApiURL(), "DEEPSEEK");
    this.connectionURL = new URL(DEEPSEEK_URL + URI_CHAT_COMPLETIONS);
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
    return DEEPSEEK_URL + URI_CHAT_COMPLETIONS;
  }
} 