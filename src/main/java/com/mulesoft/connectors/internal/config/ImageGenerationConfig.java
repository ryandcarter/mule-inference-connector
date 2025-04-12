package com.mulesoft.connectors.internal.config;

import com.mulesoft.connectors.internal.connection.types.ImageGenerationProvider;
import com.mulesoft.connectors.internal.models.images.ModelNameProvider;
import com.mulesoft.connectors.internal.models.images.ModelTypeProvider;
import com.mulesoft.connectors.internal.operations.ImageGenerationModelOperations;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;


@Configuration(name="image-generation-config")
@ConnectionProviders({ImageGenerationProvider.class})
@Operations(ImageGenerationModelOperations.class)
public class ImageGenerationConfig {
}
