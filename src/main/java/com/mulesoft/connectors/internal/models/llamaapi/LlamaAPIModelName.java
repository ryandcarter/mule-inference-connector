package com.mulesoft.connectors.internal.models.llamaapi;

public enum LlamaAPIModelName {
    // only using chat model types.
    LLAMA3_1_70B("llama3.1-70b", true);

    private final String value;
    private final boolean textGenerationSupport;

    LlamaAPIModelName(String value, boolean textGenerationSupport) {
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