package com.mulesoft.connectors.internal.config;

import com.mulesoft.connectors.internal.models.images.ModelNameProvider;
import com.mulesoft.connectors.internal.models.images.ModelTypeProvider;
import com.mulesoft.connectors.internal.operations.ImageGenerationOperations;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;

@Operations(ImageGenerationOperations.class)
@Configuration(name="image-generation-config")
public class ImageGenerationConfiguration {

	@Parameter
	@Placement(order = 1, tab = Placement.DEFAULT_TAB)
	@Expression(ExpressionSupport.SUPPORTED)
	@DisplayName("Inference Type")
	@OfValues(ModelTypeProvider.class)
	private String inferenceType;

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@DisplayName("API Key")
	private String apiKey;

	public String getApiKey() {
		return apiKey;
	}

	public String getInferenceType() {
		return inferenceType;
	}

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@DisplayName("Image Generation Model")
	@OfValues(ModelNameProvider.class)
	@Optional(defaultValue = "gpt-4o-mini")
	private String modelName;

	public String getModelName() {
		return modelName;
	}

}





