package com.mulesoft.connectors.internal.config;

import com.mulesoft.connectors.internal.connection.types.VisionProvider;
import com.mulesoft.connectors.internal.operations.VisionModelOperations;
import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;


@Configuration(name="vision-config")
@ConnectionProviders({VisionProvider.class})
@Operations(VisionModelOperations.class)
public class VisionConfig {
}
