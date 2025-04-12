package com.mulesoft.connectors.internal.config.options;

import com.mulesoft.connectors.internal.constants.InferenceConstants;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

public class LMStudio {
  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional(defaultValue = InferenceConstants.LMSTUDIO_URL)
  @DisplayName("LM Studio Base URL")
  private String lmStudio;

  public String getLmStudio() { return lmStudio; }
  public void setLmStudio(String lmStudio) { this.lmStudio = lmStudio; }

}
