package com.mulesoft.connectors.internal.models.nvidia.providers;

import com.mulesoft.connectors.internal.models.nvidia.NVidiaModelName;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;

import java.util.Arrays;
import java.util.Set;

public class NVidiaTextGenerationModelNameProvider implements ValueProvider {

  @Override
  public Set<Value> resolve() {
    return ValueBuilder.getValuesFor(Arrays.stream(NVidiaModelName.values())
            .filter(NVidiaModelName::isTextGenerationSupport).map(String::valueOf));
  }
} 