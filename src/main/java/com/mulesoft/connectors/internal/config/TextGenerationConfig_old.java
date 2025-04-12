package com.mulesoft.connectors.internal.config;

import com.mulesoft.connectors.internal.config.options.*;
import com.mulesoft.connectors.internal.connection.types.TextGenerationProvider;
import com.mulesoft.connectors.internal.operations.TextGenerationOperations;
import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.Operations;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;


@Configuration(name="text-generation-config")
@ConnectionProviders({TextGenerationProvider.class})
@Operations(TextGenerationOperations.class)
public class TextGenerationConfig_old {


    @ParameterGroup(name= "Azure AI Foundry")
    private AzureModelInference azureModelInference;

    public AzureModelInference getAzureModelInference() {
        return azureModelInference;
    }

    public void setAzureModelInference(AzureModelInference azureModelInference) {
        this.azureModelInference = azureModelInference;
    }

    @ParameterGroup(name= "Azure OpenAI")
    private AzureOpenAI azureOpenAI;

    public AzureOpenAI getAzureOpenAI() {
        return azureOpenAI;
    }

    public void setAzureOpenAI(AzureOpenAI azureOpenAI) {
        this.azureOpenAI = azureOpenAI;
    }

    @ParameterGroup(name= "Docker Models")
    private DockerModels dockerModels;

    public DockerModels getDockerModels() {
        return dockerModels;
    }

    public void setDockerModels(DockerModels dockerModels) {
        this.dockerModels = dockerModels;
    }

    @ParameterGroup(name= "GPT4ALL")
    private GPT4ALL gpt4All;

    public GPT4ALL getGPT4ALL() {
        return gpt4All;
    }

    public void setGPT4ALL(GPT4ALL gpt4All) {
        this.gpt4All = gpt4All;
    }

    @ParameterGroup(name= "LM Studio")
    private LMStudio lmStudio;

    public LMStudio getLMStudio() {
        return lmStudio;
    }

    public void setLMStudio(LMStudio lmStudio) {
        this.lmStudio = lmStudio;
    }


    @ParameterGroup(name= "Ollama Parameters")
    private Ollama ollama;

    public Ollama getOllama() {
        return ollama;
    }

    public void setOllama(Ollama ollama) {
        this.ollama = ollama;
    }


    @ParameterGroup(name= "OpenAI Compatible Endpoint")
    private OpenAICompatibleEndpoint openAICompatibleEndpoint;

    public OpenAICompatibleEndpoint getOpenAICompatibleEndpoint() {
        return openAICompatibleEndpoint;
    }

    public void setOpenAICompatibleEndpoint(OpenAICompatibleEndpoint openAICompatibleEndpoint) {
        this.openAICompatibleEndpoint = openAICompatibleEndpoint;
    }



    @ParameterGroup(name= "Portkey")
    private Portkey portkey;

    public Portkey getPortkey() {
        return portkey;
    }

    public void setPortkey(Portkey portkey) {
        this.portkey = portkey;
    }

    @ParameterGroup(name = "Xinference", showInDsl = false)
    private Xinference xinference;

    public Xinference getXinference() {
        return xinference;
    }

    public void setXinference(Xinference xinference) {
        this.xinference = xinference;
    }

}
