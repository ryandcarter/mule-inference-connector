package com.mulesoft.connectors.internal.connection.types;

import com.mulesoft.connectors.internal.config.options.AzureModelInference;
import com.mulesoft.connectors.internal.connection.BaseConnection;
import org.mule.runtime.api.connection.ConnectionException;
import org.mule.runtime.http.api.client.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Vision implements BaseConnection {

  private static Logger LOGGER = LoggerFactory.getLogger(Vision.class);

  private HttpClient httpClient;
  private int timeout;
  private String inferenceType;
  private String apiKey;
  private String modelName;
  private Number maxTokens;
  private Number temperature;
  private Number topP;


  public Vision(
          HttpClient httpClient,
          int timeout,
          String inferenceType,
          String apiKey,
          String modelName,
          Number maxTokens,
          Number temperature,
          Number topP
  ) {

    this.httpClient = httpClient;
    this.timeout = timeout;
    this.inferenceType = inferenceType;
    this.apiKey = apiKey;
    this.modelName = modelName;
    this.maxTokens = maxTokens;
    this.temperature = temperature;
    this.topP = topP;
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


  public boolean validate() throws ConnectionException {
    return true;
  }

}
