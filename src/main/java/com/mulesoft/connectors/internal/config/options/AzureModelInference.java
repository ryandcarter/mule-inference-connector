package com.mulesoft.connectors.internal.config.options;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

public class AzureModelInference {

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional
  @DisplayName("API Version")
  private String azureAIFoundryApiVersion;

  public String getAzureAIFoundryApiVersion() { return azureAIFoundryApiVersion; }
  public void setAzureAIFoundryApiVersion(String azureAIFoundryApiVersion) { this.azureAIFoundryApiVersion = azureAIFoundryApiVersion; }

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional
  @DisplayName("Resource Name")
  private String azureAIFoundryResourceName;

  public String getAzureAIFoundryResourceName() { return azureAIFoundryResourceName; }
  public void setAzureAIFoundryResourceName(String azureAIFoundryResourceName) { this.azureAIFoundryResourceName = azureAIFoundryResourceName; }

}
