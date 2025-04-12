package com.mulesoft.connectors.internal.config;

import com.mulesoft.connectors.internal.connection.types.TextGenerationProvider;
import com.mulesoft.connectors.internal.constants.InferenceConstants;
import com.mulesoft.connectors.internal.operations.TextGenerationOperations;
import org.mule.runtime.api.meta.ExpressionSupport;
import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Expression;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.Optional;
import org.mule.runtime.extension.api.annotation.param.Parameter;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;
import org.mule.runtime.extension.api.annotation.param.display.DisplayName;
import org.mule.runtime.extension.api.annotation.param.display.Placement;


@Configuration(name="text-generation-config")
@ConnectionProviders({TextGenerationProvider.class})
@Operations(TextGenerationOperations.class)
public class TextGenerationConfig {

    @Parameter
    @Expression(ExpressionSupport.SUPPORTED)
    @Optional
    @Placement(tab = "Additional Properties")
    @DisplayName("[Azure AI Foundry] API Version")
    private String azureAIFoundryApiVersion;

    public String getAzureAIFoundryApiVersion() { return azureAIFoundryApiVersion; }
    public void setAzureAIFoundryApiVersion(String azureAIFoundryApiVersion) { this.azureAIFoundryApiVersion = azureAIFoundryApiVersion; }

    @Parameter
    @Expression(ExpressionSupport.SUPPORTED)
    @Optional
    @Placement(tab = "Additional Properties")
    @DisplayName("[Azure AI Foundry] Resource Name")
    private String azureAIFoundryResourceName;

    public String getAzureAIFoundryResourceName() { return azureAIFoundryResourceName; }
    public void setAzureAIFoundryResourceName(String azureAIFoundryResourceName) { this.azureAIFoundryResourceName = azureAIFoundryResourceName; }

    @Parameter
    @Expression(ExpressionSupport.SUPPORTED)
    @Optional
    @Placement(tab = "Additional Properties")
    @DisplayName("[Azure OpenAI] Deployment ID")
    private String azureOpenaiDeploymentId;

    public String getAzureOpenaiDeploymentId() { return azureOpenaiDeploymentId; }
    public void setAzureOpenaiDeploymentId(String azureOpenaiDeploymentId) { this.azureOpenaiDeploymentId = azureOpenaiDeploymentId; }

    @Parameter
    @Expression(ExpressionSupport.SUPPORTED)
    @Optional
    @Placement(tab = "Additional Properties")
    @DisplayName("[Azure OpenAI] Resource Name")
    private String azureOpenaiResourceName;

    public String getAzureOpenaiResourceName() { return azureOpenaiResourceName; }
    public void setAzureOpenaiResourceName(String azureOpenaiResourceName) { this.azureOpenaiResourceName = azureOpenaiResourceName; }

    @Parameter
    @Expression(ExpressionSupport.SUPPORTED)
    @Optional(defaultValue = InferenceConstants.DOCKER_MODEL_URL)
    @Placement(tab = "Additional Properties")
    @DisplayName("[Docker Models] Base URL")
    private String dockerModelUrl;

    public String getDockerModelUrl() { return dockerModelUrl; }
    public void setDockerModelUrl(String dockerModelUrl) { this.dockerModelUrl = dockerModelUrl; }

    @Parameter
    @Expression(ExpressionSupport.SUPPORTED)
    @Optional(defaultValue = InferenceConstants.GPT4ALL_URL)
    @Placement(tab = "Additional Properties")
    @DisplayName("[GPT4ALL] Base URL")
    private String gpt4All;

    public String getGpt4All() { return gpt4All; }
    public void setGpt4All(String gpt4All) { this.gpt4All = gpt4All; }


    @Parameter
    @Expression(ExpressionSupport.SUPPORTED)
    @Optional(defaultValue = InferenceConstants.LMSTUDIO_URL)
    @Placement(tab = "Additional Properties")
    @DisplayName("[LM Studio] Base URL")
    private String lmStudio;

    public String getLmStudio() { return lmStudio; }
    public void setLmStudio(String lmStudio) { this.lmStudio = lmStudio; }

    @Parameter
    @Expression(ExpressionSupport.SUPPORTED)
    @Optional(defaultValue = "http://localhost:11434/api")
    @Placement(tab = "Additional Properties")
    @DisplayName("[Ollama] Base URL")
    private String ollamaUrl;

    public String getOllamaUrl() { return ollamaUrl; }
    public void setOllamaUrl(String ollamaUrl) { this.ollamaUrl = ollamaUrl; }

    @Parameter
    @Expression(ExpressionSupport.SUPPORTED)
    @Optional(defaultValue = InferenceConstants.OPENAI_COMPATIBLE_ENDPOINT)
    @Placement(tab = "Additional Properties")
    @DisplayName("[OpenAI Compatible] Base URL")
    private String openCompatibleURL;

    public String getOpenAICompatibleURL() { return openCompatibleURL; }
    public void setOpenAICompatibleURL(String openCompatibleURL) { this.openCompatibleURL = openCompatibleURL; }

    @Parameter
    @Optional(defaultValue = "Portkey-virtual-key")
    @Expression(ExpressionSupport.SUPPORTED)
    @Placement(tab = "Additional Properties")
    @DisplayName("[Portkey] Virtual Key")
    private String virtualKey;

    public String getVirtualKey() { return virtualKey; }
    public void setVirtualKey(String virtualKey) { this.virtualKey = virtualKey; }

    @Parameter
    @Expression(ExpressionSupport.SUPPORTED)
    @Optional(defaultValue = "http://127.0.0.1:9997/v1 or https://inference.top/api/v1")
    @Placement(tab = "Additional Properties")
    @DisplayName("[Xinference] Base URL")
    private String xnferenceUrl;

    public String getxinferenceUrl() { return xnferenceUrl; }
    public void setXinferenceUrl(String xnferenceUrl) { this.xnferenceUrl = xnferenceUrl; }

}
