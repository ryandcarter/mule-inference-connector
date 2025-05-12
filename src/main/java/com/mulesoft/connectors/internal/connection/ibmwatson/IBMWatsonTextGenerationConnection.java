package com.mulesoft.connectors.internal.connection.ibmwatson;

import com.mulesoft.connectors.internal.connection.TextGenerationConnection;
import org.mule.runtime.http.api.client.HttpClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class IBMWatsonTextGenerationConnection extends TextGenerationConnection {

  private static final String URI_CHAT_COMPLETIONS = "/chat/completions";
  public static final String IBMWATSON_URL = "https://api.ibm.com/watson/v1";

  private final URL connectionURL;

  public IBMWatsonTextGenerationConnection(HttpClient httpClient, String modelName, String apiKey,
                                         Number temperature, Number topP,
                                         Number maxTokens, Map<String, String> mcpSseServers, int timeout)
          throws MalformedURLException {
    super(httpClient, apiKey, modelName, maxTokens, temperature, topP, timeout, mcpSseServers, fetchApiURL(), "IBMWATSON");
    this.connectionURL = new URL(IBMWATSON_URL + URI_CHAT_COMPLETIONS);
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
    return IBMWATSON_URL + URI_CHAT_COMPLETIONS;
  }
} 