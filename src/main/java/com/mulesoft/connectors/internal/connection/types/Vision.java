package com.mulesoft.connectors.internal.connection.types;

import com.mulesoft.connectors.internal.connection.ChatCompletionBase;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.http.api.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vision implements ChatCompletionBase {

  private static Logger LOGGER = LoggerFactory.getLogger(Vision.class);

  private HttpClient httpClient;
  private int timeout;
  private String inferenceType;
  private String apiKey;
  private String modelName;
  private Number maxTokens;
  private Number temperature;
  private Number topP;
  private String ollamaUrl;
  private String openCompatibleURL;
  private String virtualKey;


  public Vision(
          HttpClient httpClient,
          int timeout,
          String inferenceType,
          String apiKey,
          String modelName,
          Number maxTokens,
          Number temperature,
          Number topP,
          String ollamaUrl,
          String openCompatibleURL,
          String virtualKey
  ) {

    this.httpClient = httpClient;
    this.timeout = timeout;
    this.inferenceType = inferenceType;
    this.apiKey = apiKey;
    this.modelName = modelName;
    this.maxTokens = maxTokens;
    this.temperature = temperature;
    this.topP = topP;
    this.ollamaUrl = ollamaUrl;
    this.openCompatibleURL = openCompatibleURL;
    this.virtualKey = virtualKey;
  }

  public HttpClient getHttpClient() {
    return httpClient;
  }


  public int getTimeout() {
    return timeout;
  }


  public String getInferenceType() {
    return inferenceType;
  }

  public String getApiKey() {
    return apiKey;
  }


  public String getModelName(){
    return modelName;
  }

  public Number getMaxTokens() {
    return maxTokens;
  }

  public Number getTemperature() {
    return temperature;
  }

  public Number getTopP() {
    return topP;
  }

  @Override
  public String getAzureAIFoundryApiVersion() {
    return "";
  }

  @Override
  public String getAzureAIFoundryResourceName() {
    return "";
  }

  @Override
  public String getAzureOpenaiDeploymentId() {
    return "";
  }

  @Override
  public String getAzureOpenaiResourceName() {
    return "";
  }

  @Override
  public String getDockerModelUrl() {
    return "";
  }

  @Override
  public String getGpt4All() {
    return "";
  }

  @Override
  public String getIBMWatsonApiVersion() {
    return "";
  }

  @Override
  public String getibmWatsonProjectID() {
    return "";
  }

  @Override
  public String getLmStudio() {
    return "";
  }

  public String getOllamaUrl() { return ollamaUrl; }

  public String getOpenAICompatibleURL() { return openCompatibleURL; }

  public String getVirtualKey() { return virtualKey; }

  @Override
  public String getxinferenceUrl() {
    return "";
  }

  public boolean validate() throws ConnectionException {
    return true;
  }

}
