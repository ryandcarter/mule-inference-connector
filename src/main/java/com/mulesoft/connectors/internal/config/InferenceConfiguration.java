package com.mulesoft.connectors.internal.config;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.slf4j.LoggerFactory;

import com.mulesoft.connectors.internal.operations.InferenceOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Operations(InferenceOperations.class)
public class InferenceConfiguration {
  private static final Logger LOGGER = LoggerFactory.getLogger(InferenceConfiguration.class);

  @Parameter
  private String apiKey;

  public String getApiKey(){
    return apiKey;
  }

  @Parameter
  private String virtualKey;

  public String getVirtualKey(){
    return virtualKey;
  }

}
