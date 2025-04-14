package com.mulesoft.connectors.internal.config;

import com.mulesoft.connectors.internal.connection.types.TextGenerationProvider;
import com.mulesoft.connectors.internal.operations.TextGenerationOperations;
import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;


@Configuration(name="text-generation-config")
@ConnectionProviders({TextGenerationProvider.class})
@Operations(TextGenerationOperations.class)
public class TextGenerationConfig {
}
