package com.mulesoft.connectors.internal.config;

import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Parameter;

import com.mulesoft.connectors.internal.operations.PortkeyAIGatewayOperations;

/**
 * This class represents an extension configuration, values set in this class are commonly used across multiple
 * operations since they represent something core from the extension.
 */
@Operations(PortkeyAIGatewayOperations.class)
public class PortkeyAIGatewayConfiguration {

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
