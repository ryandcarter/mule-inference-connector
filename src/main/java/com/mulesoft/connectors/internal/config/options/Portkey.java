package com.mulesoft.connectors.internal.config.options;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;

public class Portkey {
  @Parameter
  @Optional(defaultValue = "Portkey-virtual-key")
  @Expression(ExpressionSupport.SUPPORTED)
  @DisplayName("Virtual Key")
  private String virtualKey;

  public String getVirtualKey() { return virtualKey; }
  public void setVirtualKey(String virtualKey) { this.virtualKey = virtualKey; }

}
