package com.mulesoft.connectors.internal.connection.chatglm;

import com.mulesoft.connectors.internal.connection.TextGenerationConnection;
import org.mule.runtime.http.api.client.HttpClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class CHATGLMTextGenerationConnection extends TextGenerationConnection {

  private static final String URI_CHAT_COMPLETIONS = "/chat/completions";
  public static final String CHATGLM_URL = "https://api.chatglm.cn/v1";

  private final URL connectionURL;

  public CHATGLMTextGenerationConnection(HttpClient httpClient, String modelName, String apiKey,
                                         Number temperature, Number topP,
                                         Number maxTokens, Map<String, String> mcpSseServers, int timeout)
          throws MalformedURLException {
    super(httpClient, apiKey, modelName, maxTokens, temperature, topP, timeout, mcpSseServers, fetchApiURL(), "CHATGLM");
    this.connectionURL = new URL(CHATGLM_URL + URI_CHAT_COMPLETIONS);
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
    return CHATGLM_URL + URI_CHAT_COMPLETIONS;
  }
} 