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
    String getAzureAIFoundryApiVersion();
    String getAzureAIFoundryResourceName();
    String getAzureOpenaiDeploymentId();
    String getAzureOpenaiResourceName();
    String getDataBricksModelUrl();
    String getDockerModelUrl();
    String getGpt4All();
    String getIBMWatsonApiVersion();
    String getibmWatsonProjectID();
    String getLmStudio();
    String getOllamaUrl();
    String getOpenAICompatibleURL();
    String getVirtualKey();
    String getxinferenceUrl();
}
