package com.mulesoft.connectors.internal.models.vertexai;

public enum VertexAIExpressModelName {
    GEMINI_20_FLASH_001("gemini-2.0-flash-001", true),
    GEMINI_20_FLASH_LITE_001("gemini-2.0-flash-lite-001", true),
    GEMINI_20_PRO_EXP_02_05("gemini-2.0-pro-exp-02-05", true),  //Experimental
    GEMINI_15_FLASH_002("gemini-1.5-flash-002", true),  //expires 9/24/25
    GEMINI_15_PRO_002("gemini-1.5-pro-002", true),      //expires 9/24/25
    GEMINI_10_PRO_002("gemini-1.0-pro-002", true);      //expires 4/9/25

    private final String value;
    private final boolean textGenerationSupport;

    VertexAIExpressModelName(String value, boolean textGenerationSupport) {
        this.value = value;
        this.textGenerationSupport = textGenerationSupport;
    }

    public boolean isTextGenerationSupport() {
        return textGenerationSupport;
    }

    @Override
    public String toString() {
        return this.value;
    }
} 