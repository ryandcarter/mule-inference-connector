package com.mulesoft.connectors.internal.config;

import com.mulesoft.connectors.internal.constants.InferenceConstants;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;
import org.mule.runtime.extension.api.annotation.values.OfValues;
import com.mulesoft.connectors.internal.models.ModelNameProvider;
import com.mulesoft.connectors.internal.models.ModelTypeProvider;
import com.mulesoft.connectors.internal.operations.obsolete_InferenceOperations;

@Operations(obsolete_InferenceOperations.class)
@Configuration(name="llm-config")
public class obsolete_InferenceConfiguration {

	@Parameter
	@Placement(order = 1, tab = Placement.DEFAULT_TAB)
	@Expression(ExpressionSupport.SUPPORTED)
	@DisplayName("Inference Type")
	@OfValues(ModelTypeProvider.class)
	private String inferenceType;

	public String getInferenceType() { return inferenceType; }
	public void setInferenceType(String inferenceType) { this.inferenceType = inferenceType; }

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@DisplayName("API Key")
	private String apiKey;

	public String getApiKey() { return apiKey; }
	public void setApiKey(String apiKey) { this.apiKey = apiKey; }

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@OfValues(ModelNameProvider.class)
	@Optional(defaultValue = "gpt-3.5-turbo")
	private String modelName;

	public String getModelName() { return modelName; }
	public void setModelName(String modelName) { this.modelName = modelName; }

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = "500")
	private Number maxTokens;

	public Number getMaxTokens() { return maxTokens; }
	public void setMaxTokens(Number maxTokens) { this.maxTokens = maxTokens; }

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = "0.9")
	private Number temperature;

	public Number getTemperature() { return temperature; }
	public void setTemperature(Number temperature) { this.temperature = temperature; }

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = "0.9")
	private Number topP;

	public Number getTopP() { return topP; }
	public void setTopP(Number topP) { this.topP = topP; }

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@DisplayName("Timeout (milliseconds)")
	@Optional(defaultValue = "60000")
	private String timeout;

	public String getTimeout() { return timeout; }
	public void setTimeout(String timeout) { this.timeout = timeout; }

	@Parameter
	@Placement(order = 1, tab = "Portkey Parameters")
	@Optional(defaultValue = "Portkey-virtual-key")
	@Expression(ExpressionSupport.SUPPORTED)
	@DisplayName("Virtual Key")
	private String virtualKey;

	public String getVirtualKey() { return virtualKey; }
	public void setVirtualKey(String virtualKey) { this.virtualKey = virtualKey; }

	@Parameter
	@Placement(order = 1, tab = "Ollama Parameters")
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = "http://localhost:11434/api")
	@DisplayName("Ollama Base URL")
	private String ollamaUrl;

	public String getOllamaUrl() { return ollamaUrl; }
	public void setOllamaUrl(String ollamaUrl) { this.ollamaUrl = ollamaUrl; }

	@Parameter
	@Placement(order = 1, tab = "GPT4ALL Parameters")
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = InferenceConstants.GPT4ALL_URL)
	@DisplayName("GPT4ALL Base URL")
	private String gpt4All;

	public String getGpt4All() { return gpt4All; }
	public void setGpt4All(String gpt4All) { this.gpt4All = gpt4All; }

	@Parameter
	@Placement(order = 1, tab = "OpenAI Compatible Endpoint")
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = InferenceConstants.OPENAI_COMPATIBLE_ENDPOINT)
	@DisplayName("OpenAI Compatible URL")
	private String openCompatibleURL;

	public String getOpenAICompatibleURL() { return openCompatibleURL; }
	public void setOpenAICompatibleURL(String openCompatibleURL) { this.openCompatibleURL = openCompatibleURL; }


	@Parameter
	@Placement(order = 1, tab = "LM Studio Parameters")
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = InferenceConstants.LMSTUDIO_URL)
	@DisplayName("LM Studio Base URL")
	private String lmStudio;

	public String getLmStudio() { return lmStudio; }
	public void setLmStudio(String lmStudio) { this.lmStudio = lmStudio; }

	@Parameter
	@Placement(order = 1, tab = "Xinference Parameters")
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = "http://127.0.0.1:9997/v1 or https://inference.top/api/v1")
	@DisplayName("Xinference Base URL")
	private String xnferenceUrl;

	public String getxinferenceUrl() { return xnferenceUrl; }
	public void setXinferenceUrl(String xnferenceUrl) { this.xnferenceUrl = xnferenceUrl; }

	@Parameter
	@Placement(order = 1, tab = "Docker Models Parameters")
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional(defaultValue = InferenceConstants.DOCKER_MODEL_URL)
	@DisplayName("Docker Models URL")
	private String dockerModelUrl;

	public String getDockerModelUrl() { return dockerModelUrl; }
	public void setDockerModelUrl(String dockerModelUrl) { this.dockerModelUrl = dockerModelUrl; }

	@Parameter
	@Placement(order = 1, tab = "Azure OpenAI Parameters")
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional
	@DisplayName("Deployment ID")
	private String azureOpenaiDeploymentId;

	public String getAzureOpenaiDeploymentId() { return azureOpenaiDeploymentId; }
	public void setAzureOpenaiDeploymentId(String azureOpenaiDeploymentId) { this.azureOpenaiDeploymentId = azureOpenaiDeploymentId; }

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@Placement(order = 2, tab = "Azure OpenAI Parameters")
	@Optional
	@DisplayName("Resource Name")
	private String azureOpenaiResourceName;

	public String getAzureOpenaiResourceName() { return azureOpenaiResourceName; }
	public void setAzureOpenaiResourceName(String azureOpenaiResourceName) { this.azureOpenaiResourceName = azureOpenaiResourceName; }

	@Parameter
	@Placement(order = 1, tab = "Azure AI Foundry Parameters")
	@Expression(ExpressionSupport.SUPPORTED)
	@Optional
	@DisplayName("API Version")
	private String azureAIFoundryApiVersion;

	public String getAzureAIFoundryApiVersion() { return azureAIFoundryApiVersion; }
	public void setAzureAIFoundryApiVersion(String azureAIFoundryApiVersion) { this.azureAIFoundryApiVersion = azureAIFoundryApiVersion; }

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@Placement(order = 2, tab = "Azure AI Foundry Parameters")
	@Optional
	@DisplayName("Resource Name")
	private String azureAIFoundryResourceName;

	public String getAzureAIFoundryResourceName() { return azureAIFoundryResourceName; }
	public void setAzureAIFoundryResourceName(String azureAIFoundryResourceName) { this.azureAIFoundryResourceName = azureAIFoundryResourceName; }

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@Placement(order = 1, tab = "IBM Watson Parameters")
	@Optional
	@DisplayName("API Version")
	private String ibmWatsonApiVersion;

	public String getIBMWatsonApiVersion() { return ibmWatsonApiVersion; }
	public void setIBMWatsonApiVersion(String ibmWatsonApiVersion) { this.ibmWatsonApiVersion = ibmWatsonApiVersion; }

	@Parameter
	@Expression(ExpressionSupport.SUPPORTED)
	@Placement(order = 2, tab = "IBM Watson Parameters")
	@Optional
	@DisplayName("Project ID")
	private String ibmWatsonProjectID;
	
	public String getibmWatsonProjectID() { return ibmWatsonProjectID; }
	public void setibmWatsonProjectID(String ibmWatsonProjectID) { this.ibmWatsonProjectID = ibmWatsonProjectID; }
}
