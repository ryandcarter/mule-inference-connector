package com.mulesoft.connectors.internal.config.options;

import com.mulesoft.connectors.internal.constants.InferenceConstants;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

public class DockerModels {
  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional(defaultValue = InferenceConstants.DOCKER_MODEL_URL)
  @DisplayName("Docker Models URL")
  private String dockerModelUrl;

  public String getDockerModelUrl() { return dockerModelUrl; }
  public void setDockerModelUrl(String dockerModelUrl) { this.dockerModelUrl = dockerModelUrl; }

}
