package com.mulesoft.connectors.internal.config;

import com.mulesoft.connectors.internal.constants.InferenceConstants;
import com.mulesoft.connectors.internal.models.vision.ModelNameProvider;
import com.mulesoft.connectors.internal.models.vision.ModelTypeProvider;
import com.mulesoft.connectors.internal.operations.obsolete_VisionOperations;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;

@Operations(obsolete_VisionOperations.class)
@Configuration(name="vision-config")
public class obsolete_VisionConfiguration {

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
	@DisplayName("Vision Model")
	@OfValues(ModelNameProvider.class)
	@Optional(defaultValue = "gpt-4o-mini")
	private String modelName;

	public String getModelName() {
		return modelName;
	}

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@DisplayName("Max. Output Token")
	@Optional(defaultValue = "500")
	private Number maxTokens;

	public Number getMaxTokens() {
		return maxTokens;
	}

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@DisplayName("Temperature")
	@Optional(defaultValue = "0.9")
	private Number temperature;

	public Number getTemperature() {
		return temperature;
	}

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@DisplayName("TopP")
	@Optional(defaultValue = "0.9")
	private Number topP;

	public Number getTopP() {
		return topP;
	}


	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@DisplayName("Timeout (milliseconds)")
	@Optional(defaultValue = "60000")
	private String timeout;

	public String getTimeout() {
		return timeout;
	}

	@Parameter
	@Placement(order = 1, tab = "Portkey Parameters")
	@Optional(defaultValue = "Valid-for-portkey-only")
	@Expression(ExpressionSupport.SUPPORTED)
	@DisplayName("Virtual Key")
	private String virtualKey;

	public String getVirtualKey() {
		return virtualKey;
	}

	@Parameter
	@Placement(order = 1, tab = "Ollama Parameters")
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = "http://localhost:11434/api")
	@DisplayName("Ollama Base URL")
	private String ollamaUrl;

	public String getOllamaUrl() {
		return ollamaUrl;
	}

	@Parameter
	@Placement(order = 1, tab = "OpenAI Compatible Endpoint")
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = InferenceConstants.GPT4ALL_URL)
	@DisplayName("OpenAI Compatible URL")
	private String openCompatibleURL;

	public String getOpenAICompatibleURL() { return openCompatibleURL; }

}





