package com.mulesoft.connectors.internal.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class InferenceTool {
    @JsonProperty("type")
    private String type;
    
}
