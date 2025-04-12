package com.mulesoft.connectors.internal.connection;


import com.mulesoft.connectors.internal.config.options.AzureModelInference;
import org.mule.runtime.http.api.client.HttpClient;

public interface BaseConnection {
    HttpClient getHttpClient();
    String getInferenceType();
    String getApiKey();
    String getModelName();
    int getTimeout();
    Number getMaxTokens();
    Number getTemperature();
    Number getTopP();
}
