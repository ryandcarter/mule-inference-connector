package com.mulesoft.connectors.internal.config;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import org.slf4j.LoggerFactory;

import com.mulesoft.connectors.internal.inference.InferenceTypeProvider;
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
  @Placement(order = 1, tab = Placement.DEFAULT_TAB)
  @DisplayName("Inference Type")
  @OfValues(InferenceTypeProvider.class)
  private String llmType;

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
