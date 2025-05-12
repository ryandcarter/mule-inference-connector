package com.mulesoft.connectors.internal.connection.ibmwatson;

import com.mulesoft.connectors.internal.connection.TextGenerationConnection;
import org.mule.runtime.http.api.client.HttpClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class IBMWatsonTextGenerationConnection extends TextGenerationConnection {

  private static final String URI_CHAT_COMPLETIONS = "/chat?version={api-version}";
  public static final String IBM_WATSON_URL = "https://us-south.ml.cloud.ibm.com/ml/v1/text";
  public static final String IBM_WATSON_Token_URL = "https://iam.cloud.ibm.com/identity/token";

  private final URL connectionURL;

  public IBMWatsonTextGenerationConnection(HttpClient httpClient, String modelName, String ibmWatsonApiVersion,
                                           String apiKey, Number temperature, Number topP,
                                           Number maxTokens, Map<String, String> mcpSseServers, int timeout)
          throws MalformedURLException {
    super(httpClient, apiKey, modelName, maxTokens, temperature, topP, timeout, mcpSseServers, fetchApiURL(ibmWatsonApiVersion), "IBMWATSON");
    this.connectionURL = new URL(fetchApiURL(ibmWatsonApiVersion));
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

  private static String fetchApiURL(String ibmWatsonApiVersion) {
    String ibmwurlStr = IBM_WATSON_URL + URI_CHAT_COMPLETIONS;
    ibmwurlStr = ibmwurlStr
            .replace("{api-version}", ibmWatsonApiVersion);
    return ibmwurlStr;
  }
} 