package com.mulesoft.connectors.internal.config;

import com.mulesoft.connectors.internal.connection.types.VisionProvider;
import com.mulesoft.connectors.internal.constants.InferenceConstants;
import com.mulesoft.connectors.internal.operations.VisionModelOperations;
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


@Configuration(name="vision-config")
@ConnectionProviders({VisionProvider.class})
@Operations(VisionModelOperations.class)
public class VisionConfig {


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


}
