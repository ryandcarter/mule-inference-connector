package com.mulesoft.connectors.internal.models.chatglm.providers;

import com.mulesoft.connectors.internal.models.chatglm.CHATGLMModelName;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;

import java.util.Arrays;
import java.util.Set;

public class CHATGLMTextGenerationModelNameProvider implements ValueProvider {

  @Override
  public Set<Value> resolve() {
    return ValueBuilder.getValuesFor(Arrays.stream(CHATGLMModelName.values())
            .filter(CHATGLMModelName::isTextGenerationSupport).map(String::valueOf));
  }
} 