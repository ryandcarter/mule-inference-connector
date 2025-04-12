package com.mulesoft.connectors.internal.config.options;

import com.mulesoft.connectors.internal.constants.InferenceConstants;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

public class GPT4ALL {
  @Parameter
  //@Placement(order = 1, tab = "GPT4ALL Parameters")
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional(defaultValue = InferenceConstants.GPT4ALL_URL)
  @DisplayName("GPT4ALL Base URL")
  private String gpt4All;

  public String getGpt4All() { return gpt4All; }
  public void setGpt4All(String gpt4All) { this.gpt4All = gpt4All; }

}
