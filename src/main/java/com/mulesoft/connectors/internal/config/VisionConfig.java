package com.mulesoft.connectors.internal.config;

import com.mulesoft.connectors.internal.config.options.*;
import com.mulesoft.connectors.internal.connection.types.TextGenerationProvider;
import com.mulesoft.connectors.internal.connection.types.VisionProvider;
import org.mule.runtime.extension.api.annotation.Configuration;
import org.mule.runtime.extension.api.annotation.connectivity.ConnectionProviders;
import org.mule.runtime.extension.api.annotation.param.ParameterGroup;


//@Operations(InferenceOperations.class)
@Configuration(name="vision-config")
@ConnectionProviders({VisionProvider.class})
public class VisionConfig {


    @ParameterGroup(name= "Ollama Parameters")
    private Ollama ollama;

    public Ollama getOllama() {
        return ollama;
    }


    @ParameterGroup(name= "OpenAI Compatible Endpoint")
    private OpenAICompatibleEndpoint openAICompatibleEndpoint;

    public OpenAICompatibleEndpoint getOpenAICompatibleEndpoint() {
        return openAICompatibleEndpoint;
    }


    @ParameterGroup(name= "Portkey")
    private Portkey portkey;

    public Portkey getPortkey() {
        return portkey;
    }


}
