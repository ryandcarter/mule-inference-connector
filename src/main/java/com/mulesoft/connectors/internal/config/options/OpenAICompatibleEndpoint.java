package com.mulesoft.connectors.internal.config.options;

import com.mulesoft.connectors.internal.constants.InferenceConstants;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

public class OpenAICompatibleEndpoint {
  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional(defaultValue = InferenceConstants.OPENAI_COMPATIBLE_ENDPOINT)
  @DisplayName("OpenAI Compatible URL")
  private String openCompatibleURL;

  public String getOpenAICompatibleURL() { return openCompatibleURL; }
  public void setOpenAICompatibleURL(String openCompatibleURL) { this.openCompatibleURL = openCompatibleURL; }

}
