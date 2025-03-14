package com.mulesoft.connectors.internal.api.model;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InferenceRequest {
    @JsonProperty("model")
    private String model;

    @JsonProperty("messages")
    private List<InferenceMessage> messages;


    @JsonProperty("max_completion_tokens")
    private String maxCompletionTokens;

    @JsonProperty("temperature")
    private String temperature;

    @JsonProperty("max_tokens")
    private String maxTokens;

    @JsonProperty("top_p")
    private String topP;

    @JsonProperty("tools")
    private List<InferenceTool> tools;

    @JsonProperty("stream")
    private boolean stream;
    
    
    
    
    
}
