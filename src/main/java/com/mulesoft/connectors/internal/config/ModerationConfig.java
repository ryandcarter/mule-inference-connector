package com.mulesoft.connectors.internal.config;

import com.mulesoft.connectors.internal.connection.types.ImageGenerationProvider;
import com.mulesoft.connectors.internal.connection.types.ModerationProvider;
import com.mulesoft.connectors.internal.operations.ImageGenerationModelOperations;
import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;


@Configuration(name="moderation-config")
@ConnectionProviders({ModerationProvider.class})
@Operations(ImageGenerationModelOperations.class)
public class ModerationConfig {
}
