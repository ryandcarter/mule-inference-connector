package com.mulesoft.connectors.internal.config.options;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

public class Ollama {

  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
  @Optional(defaultValue = "http://localhost:11434/api")
  @DisplayName("Ollama Base URL")
  private String ollamaUrl;

  public String getOllamaUrl() { return ollamaUrl; }
  public void setOllamaUrl(String ollamaUrl) { this.ollamaUrl = ollamaUrl; }


}
