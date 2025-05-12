package com.mulesoft.connectors.internal.models.llamaapi.providers;

import com.mulesoft.connectors.internal.models.llamaapi.LlamaAPIModelName;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;

import java.util.Arrays;
import java.util.Set;

public class LlamaAPITextGenerationModelNameProvider implements ValueProvider {

  @Override
  public Set<Value> resolve() {
    return ValueBuilder.getValuesFor(Arrays.stream(LlamaAPIModelName.values())
            .filter(LlamaAPIModelName::isTextGenerationSupport).map(String::valueOf));
  }
} 