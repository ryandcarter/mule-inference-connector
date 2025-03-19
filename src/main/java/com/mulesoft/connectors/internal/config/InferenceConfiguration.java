package com.mulesoft.connectors.internal.config;

import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import com.mulesoft.connectors.internal.models.moderation.ModerationNameProvider;

import com.mulesoft.connectors.internal.models.ModelNameProvider;
import com.mulesoft.connectors.internal.models.ModelTypeProvider;
import com.mulesoft.connectors.internal.operations.InferenceOperations;

@Operations(InferenceOperations.class)
public class InferenceConfiguration {

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
	@OfValues(ModelNameProvider.class)
	@Optional(defaultValue = "gpt-3.5-turbo")
	private String modelName;

	public String getModelName() {
		return modelName;
	}

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = "500")
	private Number maxTokens;

	public Number getMaxTokens() {
		return maxTokens;
	}

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = "0.9")
	private Number temperature;

	public Number getTemperature() {
		return temperature;
	}

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = "0.9")
	private Number topP;

	public Number getTopP() {
		return topP;
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
	@Placement(order = 1, tab = "Xinference Parameters")
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = "http://127.0.0.1:9997/v1 or https://inference.top/api/v1")
	@DisplayName("Xinference Base URL")
	private String xnferenceUrl;

	public String getxinferenceUrl() {
		return xnferenceUrl;
	}

	@Parameter  
	@Placement(order = 1, tab = "Azure OpenAI Parameters")
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional
	@DisplayName("Deployment ID")
	private String azureOpenaiDeploymentId;

	public String getAzureOpenaiDeploymentId() {
		return azureOpenaiDeploymentId;
	}
    
  @Parameter
  @Expression(ExpressionSupport.SUPPORTED)
	@Placement(order = 2, tab = "Azure OpenAI Parameters")
	@Optional
  @DisplayName("Resource Name")
  private String azureOpenaiResourceName;

  public String getAzureOpenaiResourceName() {
     return azureOpenaiResourceName;
  }
}





