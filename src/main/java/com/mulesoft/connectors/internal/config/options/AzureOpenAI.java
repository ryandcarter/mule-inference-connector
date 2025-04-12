package com.mulesoft.connectors.internal.config.options;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

public class AzureOpenAI {
  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional
  @DisplayName("Deployment ID")
  private String azureOpenaiDeploymentId;

  public String getAzureOpenaiDeploymentId() { return azureOpenaiDeploymentId; }
  public void setAzureOpenaiDeploymentId(String azureOpenaiDeploymentId) { this.azureOpenaiDeploymentId = azureOpenaiDeploymentId; }

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional
  @DisplayName("Resource Name")
  private String azureOpenaiResourceName;

  public String getAzureOpenaiResourceName() { return azureOpenaiResourceName; }
  public void setAzureOpenaiResourceName(String azureOpenaiResourceName) { this.azureOpenaiResourceName = azureOpenaiResourceName; }

}
