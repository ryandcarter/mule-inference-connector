package com.mulesoft.connectors.internal.config.options;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

public class Xinference {
  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional(defaultValue = "http://127.0.0.1:9997/v1 or https://inference.top/api/v1")
  @DisplayName("Xinference Base URL")
  private String xnferenceUrl;

  public String getxinferenceUrl() { return xnferenceUrl; }
  public void setXinferenceUrl(String xnferenceUrl) { this.xnferenceUrl = xnferenceUrl; }

}
