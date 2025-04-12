package com.mulesoft.connectors.internal.connection;


import org.mule.runtime.http.api.client.HttpClient;

public interface ChatCompletionBase {
    HttpClient getHttpClient();
    String getInferenceType();
    String getApiKey();
    String getModelName();
    int getTimeout();
    Number getMaxTokens();
    Number getTemperature();
    Number getTopP();
}
