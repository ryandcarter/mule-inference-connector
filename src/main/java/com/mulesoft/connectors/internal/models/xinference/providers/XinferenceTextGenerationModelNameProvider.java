package com.mulesoft.connectors.internal.models.xinference.providers;

import com.mulesoft.connectors.internal.models.xinference.XinferenceModelName;
import org.mule.runtime.api.value.Value;
import org.mule.runtime.extension.api.values.ValueBuilder;
import org.mule.runtime.extension.api.values.ValueProvider;

import java.util.Arrays;
import java.util.Set;

public class XinferenceTextGenerationModelNameProvider implements ValueProvider {

  @Override
  public Set<Value> resolve() {
    return ValueBuilder.getValuesFor(Arrays.stream(XinferenceModelName.values())
            .filter(XinferenceModelName::isTextGenerationSupport).map(String::valueOf));
  }
} 